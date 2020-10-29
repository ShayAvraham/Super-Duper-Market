package managers;

import dataContainers.*;
import engineLogic.*;
import exceptions.DuplicateValuesException;
import jaxb.generated.SuperDuperMarketDescriptor;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class DataManager
{
    private Map<String, Region> allRegions;
    private Map<Integer, User> allUsers;

    private final String JAXB_PACKAGE_NAME = "jaxb.generated";
    private final String FILE_NOT_EXIST_ERROR_MSG = "No xml file was found in this path: ";

    public DataManager()
    {
        this.allRegions = new HashMap<>();
        this.allUsers = new HashMap<>();
    }

    /********************************************** Load XML  ****************************************/

    public Region deserializeXMLToRegion(int ownerID,String ownerName,InputStream xmlFileInputStream) throws JAXBException, FileNotFoundException, InstanceNotFoundException
    {
        JAXBContext jc = JAXBContext.newInstance(JAXB_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        Region newRegion = new Region((SuperDuperMarketDescriptor) u.unmarshal(xmlFileInputStream),ownerID,ownerName);
        if(allRegions.putIfAbsent(newRegion.getName(),newRegion)!=null)
        {
            throw new DuplicateValuesException("region",newRegion.getName());
        }
        ((Owner)allUsers.get(ownerID)).addRegion(newRegion);
        return newRegion;
    }

    public void ChargeMoneyInUserAccount(int userId, float amountToCharge, Date transactionDate)
    {
        User user = allUsers.get(userId);
        user.addTransaction(Transaction.TransactionCategory.CHARGING, transactionDate, amountToCharge);
    }

    public Map<String, Region> getAllRegions() {
        return allRegions;
    }

    public Map<Integer, User> getAllUsers() {
        return allUsers;
    }

    /********************************************** Add New User  ***************************************/

    public User addNewUser(UserDataContainer userData)
    {
        validateUser(userData);
        User newUser = userData.getRole().equals("customer")?
                new Customer(userData.getName(),
                        userData.getBalance(),
                        new ArrayList<Transaction>()):
                new Owner(userData.getName(),
                        userData.getBalance(),
                        new ArrayList<Transaction>());
        allUsers.put(newUser.getId(),newUser);
        return newUser;
    }

    private void validateUser(UserDataContainer userData)
    {
        if(allUsers.size()!=0)
        {
            for (User user : allUsers.values())
            {
                if (user.getName().equals(userData.getName()))
                {
                    throw new DuplicateValuesException("user", user.getName());
                }
            }
        }
    }

/********************************************** Place Order Logic ****************************************/

public int getRegionOwnerID(String regionName)
{
   return allRegions.get(regionName).getOwnerID();
}


/** Dynamic Store Allocation **/

public Store getStoreWithTheCheapestPrice(String regionName, int productId)
{
    Store cheapestStore = null;
    for(Store store : allRegions.get(regionName).getStores().values())
    {
        StoreProduct product = store.getProductById(productId);
        if(product != null)
        {
            if(cheapestStore == null)
            {
                cheapestStore = store;
            }
            else if(cheapestStore.getProductById(productId).getPrice() > product.getPrice())
            {
                cheapestStore = store;
            }
        }
    }
    return cheapestStore;
}


    /** Available Discounts **/

    public Discount getDiscount(String regionName, int storeID, String discountName)
    {
       return allRegions.get(regionName).getStores().get(storeID).getDiscountByName(discountName);
    }

                            /**     Add New Order    **/
    public Collection<User> addNewOrder(OrderDataContainer newOrderDataContainer,String regionName, Integer customerID)
    {
        Order newOrder = createNewOrder(newOrderDataContainer,regionName);
        Map <Integer,Order> newSubOrders = createSubOrders(newOrderDataContainer, newOrder.getId(),regionName);
        User customer = allUsers.get(customerID);
        Collection<User> storeOwners = updateStoreOwners(newSubOrders,customer,regionName);
        Region region = allRegions.get(regionName);
        region.addNewOrder(newSubOrders);
        customer.addTransaction(Transaction.TransactionCategory.TRANSFER,newOrder.getDate(),newOrder.getTotalCost());
        if(customer instanceof Customer)
        {
            ((Customer) customer).addOrder(newOrder);
        }
        return storeOwners;
    }

    private Collection<User> updateStoreOwners(Map<Integer,Order> subOrders, User customer,String regionName)
    {
        Collection<User> storeOwners = new ArrayList<>();
        for (Integer storeID: subOrders.keySet())
        {
            String storeOwnerName = allRegions.get(regionName).getStores().get(storeID).getOwnerName();
            User storeOwner = getUserByName(storeOwnerName);
            storeOwner.addTransaction(Transaction.TransactionCategory.RECEIVE,
                    subOrders.get(storeID).getDate(),
                    subOrders.get(storeID).getTotalCost());
            addNewSubOrderNoticeToStoreOwner(storeOwner,subOrders.get(storeID),customer);
            storeOwners.add(storeOwner);
        }
        return storeOwners;
    }

    private void addNewSubOrderNoticeToStoreOwner(User storeOwner, Order order, User customer)
    {
        if(storeOwner instanceof Owner)
        {
            ((Owner) storeOwner).addNotice(createOrderNoticeOrder(order,customer));
        }
    }

    private OrderNotice createOrderNoticeOrder(Order order, User customer)
    {
        return new OrderNotice(order.getId(),
                customer.getName(),
                order.getProductTypesAmountInOrder(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost());
    }


    /**     Create New Order    **/
    private Order createNewOrder(OrderDataContainer newOrderDataContainer,String regionName)
    {
        return new Order(newOrderDataContainer.getDate(),
                newOrderDataContainer.getOrderDestination(),
                newOrderDataContainer.getCustomerName(),
                newOrderDataContainer.getRegionName(),
                createOrderProducts(newOrderDataContainer.getProducts(),regionName),
                createOrderDiscounts(newOrderDataContainer.getDiscounts(),regionName),
                newOrderDataContainer.isDynamic(),
                newOrderDataContainer.getCostOfAllProducts(),
                newOrderDataContainer.getDeliveryCost(),
                newOrderDataContainer.getTotalCost());
    }

    private Map<Store, Collection<OrderProduct>> createOrderProducts(
            Map<Integer, Collection<ProductDataContainer>> products ,String regionName)
    {
        Map<Store, Collection<OrderProduct>> orderStoreAndProducts = new HashMap<>();
        for(Integer storeID: products.keySet())
        {
            Store orderStore = allRegions.get(regionName).getStores().get(storeID);
            orderStoreAndProducts.put(orderStore,createOrderStoreProducts(regionName,storeID,products.get(storeID)));
        }
        return orderStoreAndProducts;
    }


    private Collection<OrderProduct> createOrderStoreProducts(String regionName,Integer storeID,
                                                              Collection<ProductDataContainer> products)
    {
        Collection<OrderProduct> orderStoreProducts = new ArrayList<>();
        for(ProductDataContainer product : products)
        {
            orderStoreProducts.add(new OrderProduct(
                    allRegions.get(regionName).getStores().get(storeID).getProductById(product.getId()),
                    new Float(product.getAmount())));
        }
        return orderStoreProducts;
    }

    private Map<Store, Collection<Discount>> createOrderDiscounts(Map<Integer, Collection<DiscountDataContainer>> discounts
                                                                ,String regionName)
    {
        Map<Store, Collection<Discount>> orderStoreAndProducts = new HashMap<>();
        if(!discounts.isEmpty())
        {
            for (Integer storeID : discounts.keySet())
            {
                Store orderStore = allRegions.get(regionName).getStores().get(storeID);
                orderStoreAndProducts.put(orderStore, createOrderStoreDiscounts(storeID, discounts.get(storeID),regionName));
            }
        }
        return orderStoreAndProducts;
    }

    private Collection<Discount> createOrderStoreDiscounts(Integer storeID, Collection<DiscountDataContainer> discounts
                                                            ,String regionName)
    {
        Collection<Discount> orderStoreDiscounts = new ArrayList<>();
        for(DiscountDataContainer discount : discounts)
        {
            orderStoreDiscounts.add(new Discount(discount.getDiscountName(),
                    discount.getDiscountType(),
                    createDiscountProduct(storeID,discount,regionName),
                    createProductsToOffer(storeID ,discount,regionName)));
        }
        return orderStoreDiscounts;
    }

    private DiscountProduct createDiscountProduct(Integer storeID, DiscountDataContainer discount,String regionName)
    {
        return new DiscountProduct( allRegions.get(regionName).getStores().
                get(storeID).getProductById(discount.getDiscountProduct().getId()),
                discount.getAmountForDiscount());
    }

    private Collection<OfferProduct> createProductsToOffer(Integer storeID,DiscountDataContainer discount,
                                                            String regionName)
    {
        Collection<OfferProduct> offerProducts = new ArrayList<>();
        for (Integer productID : discount.getPriceForOfferProduct().keySet())
        {
            offerProducts.add(new OfferProduct(
                    allRegions.get(regionName).getStores().get(storeID).getProductById(productID),
                    discount.getPriceForOfferProduct().get(productID),
                    discount.getAmountForOfferProduct().get(productID)));
        }
        return offerProducts;
    }

    /** Create Sub Orders **/
    private Map<Integer,Order> createSubOrders(OrderDataContainer newOrderDataContainer,int orderID,String regionName)
    {
        Map <Integer,Order> subOrders = new HashMap<>();
        for(Integer storeID : newOrderDataContainer.getProducts().keySet())
        {
            subOrders.put(storeID,createSubOrder(newOrderDataContainer,orderID ,storeID,regionName));
        }
        return subOrders;
    }

    private Order createSubOrder(OrderDataContainer newOrder, int orderID, Integer storeID,String regionName)
    {
        Store store = allRegions.get(regionName).getStores().get(storeID);
        float costOfAllProducts = getStoreCostOfAllProducts(storeID,newOrder.getProducts().get(storeID),
                newOrder.getDiscounts().get(storeID),regionName);
        float deliveryCost = getDeliveryCostFromStore(storeID,newOrder.getOrderDestination(),regionName);

        return new Order(orderID,
                newOrder.getDate(),
                newOrder.getOrderDestination(),
                newOrder.getCustomerName(),
                newOrder.getRegionName(),
                new HashMap<Store,Collection<OrderProduct>>()
                {{put(store,createOrderStoreProducts(regionName,storeID,newOrder.getProducts().get(storeID)));}},
                createSubOrderDiscounts(newOrder,storeID,store,regionName),
                newOrder.isDynamic(),
                costOfAllProducts,
                deliveryCost,
                costOfAllProducts + deliveryCost);
    }


    private float getStoreCostOfAllProducts(Integer storeID,Collection<ProductDataContainer> products,
                                            Collection<DiscountDataContainer> discounts,
                                            String regionName)
    {
        float storeCostOfAllProducts = 0;
        storeCostOfAllProducts += getProductsCostFromStore(storeID,products,regionName);
        if(discounts != null)
        {
            storeCostOfAllProducts += getDiscountsCostFromStore(storeID, discounts, regionName);
        }
        return storeCostOfAllProducts;
    }

    private float getProductsCostFromStore(Integer storeID, Collection<ProductDataContainer> products,
                                          String regionName)
    {
        float cost = 0;
        Store store = allRegions.get(regionName).getStores().get(storeID);
        for (ProductDataContainer product:products)
        {
            cost += store.getProductById(product.getId()).getPrice() * product.getAmount();
        }

        return cost;
    }

        private float getDiscountsCostFromStore(Integer storeID, Collection<DiscountDataContainer> discounts,
                                                String regionName)
    {
        float costOfAllDiscounts = 0;
        for (DiscountDataContainer discount : discounts)
        {
            for(Integer productID : discount.getPriceForOfferProduct().keySet())
            {
                costOfAllDiscounts += discount.getPriceForOfferProduct().get(productID) *
                        discount.getAmountForOfferProduct().get(productID);
            }
        }
        return costOfAllDiscounts;
    }

    private float getDeliveryCostFromStore(Integer storeID, Point orderDestination , String regionName)
    {
        Store store = allRegions.get(regionName).getStores().get(storeID);
        float deliveryCost  = getDistanceBetweenStoreAndCustomer(store.getPosition(), orderDestination) * store.getPPK();
        return deliveryCost;
    }


    private float getDistanceBetweenStoreAndCustomer(Point storePosition, Point customerPosition)
    {
        return Math.abs((float) storePosition.distance(customerPosition));
    }

    private HashMap<Store,Collection<Discount>> createSubOrderDiscounts(OrderDataContainer newOrder,
                                                                        Integer storeID,
                                                                        Store store,
                                                                        String regionName)
    {
        HashMap<Store,Collection<Discount>> discounts = new HashMap<Store,Collection<Discount>>();
        if(newOrder.getDiscounts().get(storeID) != null)
        {
            discounts.put(store, createOrderStoreDiscounts(storeID, newOrder.getDiscounts().get(storeID),regionName));
        }
        return discounts;
    }

    /**     Add New Feedback    **/

    public User addNewFeedback(String regionName, Integer storeID, String customerName,
                               Integer rank, String description, Date date)
    {
        Notice newFeedback = new Feedback(regionName,storeID,customerName,rank,
                                            description,date);
        String storeOwnerName = allRegions.get(regionName).getStores().get(storeID).getOwnerName();
        User storeOwner = getUserByName(storeOwnerName);
        if(storeOwner instanceof Owner)
        {
            ((Owner) storeOwner).addNotice(newFeedback);
        }
        return storeOwner;
    }

    private User getUserByName(String userName)
    {
        for (User user: allUsers.values())
        {
            if(user.getName() == userName)
            {
                return user;
            }
        }
        return null;
    }

    /********************************************** Add New Store ****************************************/

    public void addNewStore(StoreDataContainer newStoreData,String regionName)
    {
        Store newStore = createNewStore(newStoreData,regionName);
        allRegions.get(regionName).getStores().put(newStore.getId(),newStore);
        int regionOwnerID = getRegionOwnerID(regionName);
        if((allUsers.get(regionOwnerID) instanceof  Owner) &&
                (newStore.getOwnerName() != allUsers.get(regionOwnerID).getName()))
        {
            ((Owner) allUsers.get(regionOwnerID)).addNotice(createStoreNotice(newStore,regionName));
        }
    }


    private Store createNewStore(StoreDataContainer newStoreData,String regionName)
    {
        return new Store(newStoreData.getId(),
                newStoreData.getName(),
                newStoreData.getOwnerName(),
                newStoreData.getPosition(),
                newStoreData.getPpk(),
                createStoreProducts(newStoreData.getProducts(),regionName));
    }

    private Collection<StoreProduct> createStoreProducts(Collection<ProductDataContainer> products,String regionName)
    {
        Collection<StoreProduct> storeProducts = new HashSet<>();
        for(ProductDataContainer product: products)
        {
            storeProducts.add(new StoreProduct(
                    allRegions.get(regionName).getProducts().get(product.getId()),
                    product.getNewPrice()));
        }
        return storeProducts;
    }

    private Notice createStoreNotice(Store newStore,String regionName)
    {
        return new StoreNotice(newStore.getOwnerName(),
                newStore.getName(),
                newStore.getPosition(),
                newStore.getStoreProducts().size(),
                allRegions.get(regionName).getProducts().size());
    }
}

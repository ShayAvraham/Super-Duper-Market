package managers;

import builders.DiscountDataContainerBuilder;
import builders.OrderDataContainerBuilder;
import builders.RegionDataContainerBuilder;
import builders.UserDataContainerBuilder;
import dataContainers.*;
import engineLogic.Customer;
import engineLogic.Order;
import engineLogic.Region;
import engineLogic.User;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;


public class SystemManager
{
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE = "Unable to remove this product because its sold only in this store.";
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE = "Unable to remove this product because the store sold only this product.";


    private DataManager dataManager;
    private Map <Integer, UserDataContainer> usersData;
    private Map <String, RegionDataContainer> regionsData;

 //   private Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom;

    private static DecimalFormat DECIMAL_FORMAT;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public SystemManager()
    {
        this.dataManager = new DataManager();
        usersData = new HashMap<>();
        regionsData = new HashMap<>();
    }

    /********************************************** Add New User Logic ****************************************/
    public synchronized UserDataContainer AddNewUser(UserDataContainer userData)
    {
        User user = dataManager.addNewUser(userData);
        UserDataContainer userDataContainer = UserDataContainerBuilder.createUserData(user);
        usersData.put(user.getId(), userDataContainer);
        return userDataContainer;
    }

    /********************************************** Load XML Logic ****************************************/

    public void LoadDataFromXMLFile(int ownerID,String ownerName, InputStream xmlFileInputStream) throws JAXBException, FileNotFoundException, InstanceNotFoundException//change
    {
        Region newRegion = dataManager.deserializeXMLToRegion(ownerID,ownerName,xmlFileInputStream);
        regionsData.put(newRegion.getName(),RegionDataContainerBuilder.createRegionData(usersData.get(ownerID).getName(),newRegion));
     }

    /********************************************** Load Region Data ****************************************/
    public Collection<ProductDataContainer> GetRegionProducts(String region)
    {
        return regionsData.get(region).getProductsData().values();
    }

    public Collection<StoreDataContainer> GetRegionStores(String region)
    {
        return regionsData.get(region).getStoresData().values();
    }

    public Collection<ProductDataContainer> GetStoreProducts(String regionName, int storeId)
    {
        return regionsData.get(regionName).getStoresData().get(storeId).getProducts();
    }

    public StoreDataContainer GetStore(String regionName, int storeId)//change
    {
        return regionsData.get(regionName).getStoresData().get(storeId);
    }
    /**************** Load All Regions data **************/

    public Collection<RegionDataContainer> GetAllRegions()
    {
        return regionsData.values();
    }

    /**************** Load All Users data **************/

    public Collection<UserDataContainer> GetAllUsers()
    {
        return usersData.values();
    }


    /**************** Charge User Money **************/

    public synchronized void ChargeMoneyInUserAccount(int userId, float amountToCharge, Date transactionDate)
    {
        dataManager.ChargeMoneyInUserAccount(userId, amountToCharge, transactionDate);
        UserDataContainer userDataContainer = UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(userId));
        usersData.replace(userId, userDataContainer);
    }

    public UserDataContainer GetUserByID(int userId)
    {
        return usersData.get(userId);
    }

    /********************************************** Update Products Logic ****************************************/
//
    /** Remove Product Logic **/
//    public void removeProductFromStore(StoreDataContainer store, ProductDataContainer productToRemove) throws ValidationException
//    {
//        validateRemoveProduct(store,productToRemove);
//        try
//        {
//            region.removeProductFromStore(store.getId(),productToRemove.getId());
//        }
//        finally
//        {
//            updateDataContainers();
//        }
//    }
//
//    private void validateRemoveProduct(StoreDataContainer store, ProductDataContainer product) throws ValidationException
//    {
//        if(!isOthersStoresSellThisProduct(store, product))
//        {
//            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE);
//        }
//        if(store.getProducts().size() == 1)
//        {
//            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE);
//        }
//    }
//
//    private boolean isOthersStoresSellThisProduct(StoreDataContainer selectedStore, ProductDataContainer product)
//    {
//        for(StoreDataContainer store: getAllStoresData())
//        {
//            if(!selectedStore.equals(store))
//            {
//                if(store.getProducts().contains(product))
//                {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
    /** Add Product Logic **/
//    public void addProductToStore(StoreDataContainer store, ProductDataContainer productToAdd, int price)
//    {
//        region.addProductToStore(store.getId(),productToAdd.getId(),price);
//        updateDataContainers();
//    }
//
//    /** Update Product Price Logic **/
//    public void updateProductPriceInStore(StoreDataContainer store, ProductDataContainer productToRemove, int newPrice)
//    {
//        region.updateProductPriceInStore(store.getId(), productToRemove.getId(), newPrice);
//        updateDataContainers();
//    }
//
    /********************************************** Place Order Logic ****************************************/

    /** Dynamic Store Allocation **/
    public Map<StoreDataContainer, Collection<ProductDataContainer>> dynamicStoreAllocation(String regionName, Collection<ProductDataContainer> productsToPurchase)
    {
        Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom = new HashMap();
        for (ProductDataContainer productToPurchase : productsToPurchase)
        {
            StoreDataContainer store = regionsData.get(regionName).getStoresData().
                    get(dataManager.getStoreWithTheCheapestPrice(regionName,productToPurchase.getId()).getId());

            ArrayList<ProductDataContainer> products = new ArrayList<>();
            ProductDataContainer product = store.getProductDataContainerById(productToPurchase.getId());
            products.add(product);
            if(storeToPurchaseFrom.putIfAbsent(store, products) != null)
            {
                storeToPurchaseFrom.get(store).add(product);
            }
        }
        return storeToPurchaseFrom;

    }
    /** Available Discounts **/

    public Map<StoreDataContainer,Collection<DiscountDataContainer>> getAvailableDiscounts(String regionName,
                                                                                Map<Integer, Collection<ProductDataContainer>> storeToPurchaseFrom,
                                                                                Map<Integer, Float> productsAmounts)
    {

        Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts = new HashMap<>();
        for (Integer storeID : storeToPurchaseFrom.keySet())
        {
            StoreDataContainer store = regionsData.get(regionName).getStoresData().get(storeID);
            Collection <DiscountDataContainer> availableStoreDiscounts = getAvailableDiscountsInStore(
                    regionName,store,storeToPurchaseFrom.get(storeID),productsAmounts);
            if(availableStoreDiscounts != null)
            {
                availableDiscounts.put(store,availableStoreDiscounts);
            }
        }
        return availableDiscounts;
    }

    private Collection<DiscountDataContainer> getAvailableDiscountsInStore(String regionName, StoreDataContainer store,
                                                           Collection<ProductDataContainer> products,
                                                           Map<Integer, Float> productsAmounts)
    {
        Collection<DiscountDataContainer> availableStoreDiscounts = new ArrayList<>();
        for(DiscountDataContainer discount : store.getDiscounts())
        {
            for (ProductDataContainer product : products)
            {
                if (discount.getDiscountProduct().equals(product))
                {
                    for(double i = productsAmounts.get(product.getId()); i >= discount.getAmountForDiscount(); i-=discount.getAmountForDiscount())
                    {
                        availableStoreDiscounts.add(DiscountDataContainerBuilder.createDiscountData(
                                regionsData.get(regionName).getProductsData(),
                                dataManager.getDiscount(regionName,store.getId(),discount.getDiscountName())

                        ));
                    }
                }
            }
        }
        if(availableStoreDiscounts.isEmpty())
        {
            return null;
        }
        return availableStoreDiscounts;
    }

    /**     Add New Order    **/
    public synchronized void AddNewOrder(Map<Integer, Collection<ProductDataContainer>> storesToBuyFrom,
                            Map<Integer, Collection<DiscountDataContainer>> selectedDiscounts,
                            Map<Integer, Float> productsAmounts, Date deliveryDate,
                            Integer xPosition, Integer yPosition, String orderType, Float productsCost,
                            Float deliveryCost, Float totalOrderCost, String regionName, int userID)
    {
        OrderDataContainer newOrderData = new OrderDataContainer(
                deliveryDate,
                new Point(xPosition,yPosition),
                regionName,
                createOrderDataProducts(storesToBuyFrom,productsAmounts),
                createOrderDataDiscounts(selectedDiscounts),
                orderType.toLowerCase().equals("static")? false: true,
                productsCost,
                deliveryCost,
                totalOrderCost);

        Collection <User> usersCollection = dataManager.addNewOrder(newOrderData,regionName,userID);
        updateDataContainers(regionName,userID,usersCollection);
    }

    private void updateDataContainers(String regionName,Integer customerID, Collection<User> usersCollection)
    {
        int regionOwnerID = dataManager.getRegionOwnerID(regionName);
        Region region = dataManager.getAllRegions().get(regionName);
        regionsData.replace(regionName,RegionDataContainerBuilder.createRegionData(
                usersData.get(regionOwnerID).getName(),region));
        usersData.replace(customerID,UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(customerID)));
        usersData.replace(regionOwnerID,UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(regionOwnerID)));
        for (User user : usersCollection )
        {
            usersData.replace(user.getId(),UserDataContainerBuilder.createUserData(user));
        }
    }

    private Map<Integer, Collection<ProductDataContainer>> createOrderDataProducts(
            Map<Integer, Collection<ProductDataContainer>> storesToBuyFrom, Map<Integer, Float> productsAmounts)
    {
        Map<Integer, Collection<ProductDataContainer>> storesToBuyFromWithAmounts = new HashMap<>();
        for(int storeID: storesToBuyFrom.keySet())
        {
            Collection productsWithAmounts = new ArrayList();
            for(ProductDataContainer product : storesToBuyFrom.get(storeID))
            {
                productsWithAmounts.add(new ProductDataContainer(product,productsAmounts.get(product.getId())));
            }
            storesToBuyFromWithAmounts.put(storeID,productsWithAmounts);
        }
        return storesToBuyFromWithAmounts;
    }


    private Map<Integer, Collection<DiscountDataContainer>> createOrderDataDiscounts
            (Map<Integer, Collection<DiscountDataContainer>> selectedDiscounts)
    {
        Map<Integer, Collection<DiscountDataContainer>> discounts = new HashMap<>();
        if(!selectedDiscounts.isEmpty())
        {
            for (Integer storeID : selectedDiscounts.keySet())
            {
                discounts.put(storeID,createOrderDataStoreDiscounts(storeID,selectedDiscounts.get(storeID)));
            }
        }
        return discounts;
    }


    private Collection<DiscountDataContainer> createOrderDataStoreDiscounts
            (Integer storeID, Collection<DiscountDataContainer> discounts)
    {
        Collection<DiscountDataContainer> storeDiscounts = new ArrayList<>();
        for (DiscountDataContainer discount : discounts)
        {
            if (discount.getDiscountType().equals("ONE_OF"))
            {
                storeDiscounts.add(createOrderDataStoreOneOfDiscount(discount));
            }
            else
            {
                storeDiscounts.add(discount);
            }
        }
        return storeDiscounts;
    }

    private DiscountDataContainer createOrderDataStoreOneOfDiscount(DiscountDataContainer discount)
    {
        return new DiscountDataContainer(
                discount.getDiscountName(),
                discount.getDiscountType(),
                discount.getDiscountProduct(),
                discount.getAmountForDiscount(),
                new HashMap<Integer, Integer>(){{put(discount.getSelectedOfferID(),
                        discount.getPriceForOfferProduct().get(discount.getSelectedOfferID()));}},
                new HashMap<Integer,Double>(){{put(discount.getSelectedOfferID(),
                        discount.getAmountForOfferProduct().get(discount.getSelectedOfferID()));}},
                discount.getIfYouBuyDescription(),
                discount.getThenYouGetDescription());
    }
    /**     Add New Feedback    **/
    public synchronized void addNewFeedback(String regionName, Integer storeID, String customerName,
                               Integer rank, String description, Date date)
    {
        User storeOwner = dataManager.addNewFeedback(regionName, storeID, customerName, rank, description, date);
        UserDataContainer newStoreOwner = UserDataContainerBuilder.createUserData(storeOwner);
        usersData.replace(newStoreOwner.getId(),newStoreOwner);

    }

    /********************************************** Show Orders History Logic ****************************************/

    public Collection<OrderDataContainer> gerCustomerOrders(int customerID, String regionName)
    {
        Collection<OrderDataContainer> orders = new ArrayList<>();
        User customer = dataManager.getAllUsers().get(customerID);
        if(customer instanceof Customer)
        {
            for (Order order: ((Customer) customer).getOrders())
            {
                if(order.getRegionName().equals(regionName))
                {
                    orders.add(OrderDataContainerBuilder.createOrderData(order,
                            regionsData.get(order.getRegionName()).getStoresData(),
                            regionsData.get(order.getRegionName()).getProductsData()));
                }
            }
        }
        return orders;
    }

    /********************************************** Order Details Logic ****************************************/
//
//    public int getProductPrice(StoreDataContainer store,ProductDataContainer product)
//    {
//        return region.getStores().get(store.getId()).getProductById(product.getId()).getPrice();
//    }
//
//    public Collection<DiscountDataContainer> createSubDiscounts(Collection <DiscountDataContainer> discounts)
//    {
//        Collection<DiscountDataContainer> subDiscounts = new ArrayList<>();
//        if(discounts != null)
//        {
//            for(DiscountDataContainer discount : discounts)
//            {
//                switch (discount.getDiscountType())
//                {
//                    case "ONE_OF":
//                    case "IRRELEVANT":
//                        subDiscounts.add(discount);
//                        break;
//                    case "ALL_OR_NOTHING":
//                        subDiscounts.addAll(createAllOrNothingSubDiscounts(discount));
//                        break;
//                }
//            }
//        }
//        return subDiscounts;
//    }
//
//    private Collection<DiscountDataContainer> createAllOrNothingSubDiscounts(DiscountDataContainer discount)
//    {
//        Collection<DiscountDataContainer> subDiscounts = new ArrayList<>();
//        for(ProductDataContainer offerProduct : discount.getPriceForOfferProduct().keySet())
//        {
//            subDiscounts.add(new DiscountDataContainer(discount.getDiscountName(),
//                    discount.getDiscountType(),
//                    discount.getDiscountProduct(),
//                    discount.getAmountForDiscount(),
//                    new HashMap<ProductDataContainer,Integer>(){{put(offerProduct,discount.getPriceForOfferProduct().get(offerProduct));}},
//                    new HashMap<ProductDataContainer,Double>(){{put(offerProduct,discount.getAmountForOfferProduct().get(offerProduct));}}));
//        }
//        return subDiscounts;
//    }
//
//
//
    /********************************************** Add New Store ****************************************/
//    public void ValidateStore(StoreDataContainer store)
//    {
//        validateStoreId(store.getId());
//        validateStorePosition(store.getPosition());
//        validateSelectedProducts(store.getProducts());
//    }
//
//    private void validateStorePosition(Point storePos)
//    {
//        validateNoStoreExistWithPos(storePos);
//        validateNoCustomerExistWithPos(storePos);
//    }
//
//    private void validateNoStoreExistWithPos(Point position)
//    {
//        for(StoreDataContainer store : allStoresData)
//        {
//            if(store.getPosition().equals(position))
//            {
//                throw new DuplicateValuesException("store",position);
//            }
//        }
//    }
//
//    private void validateNoCustomerExistWithPos(Point position)
//    {
//        for(UserDataContainer customer : allCustomersData)
//        {
//            if(customer.getPosition().equals(position))
//            {
//                throw new DuplicateValuesException("customer",position);
//            }
//        }
//    }
//
//    private void validateStoreId(int storeID)
//    {
//        if(region.getStores().get(storeID) != null)
//        {
//            throw new DuplicateValuesException("store",storeID);
//        }
//    }
//
//    public void addNewStore(StoreDataContainer newStoreDataContainer)
//    {
//        Store newStore = createNewStore(newStoreDataContainer);
//        region.addNewStore(newStore);
//        updateDataContainers();
//    }
//
//
//    private Store createNewStore(StoreDataContainer newStoreDataContainer)
//    {
//        return new Store(newStoreDataContainer.getId(),
//                newStoreDataContainer.getName(),
//                newStoreDataContainer.getPosition(),
//                newStoreDataContainer.getPpk(),
//                createStoreProducts(newStoreDataContainer.getProducts()));
//    }
//
//    private Collection<StoreProduct> createStoreProducts(Collection<ProductDataContainer> products)
//    {
//        Collection<StoreProduct> storeProducts = new HashSet<>();
//        for(ProductDataContainer product: products)
//        {
//            storeProducts.add(new StoreProduct(
//                    region.getProducts().get(product.getId()),
//                    product.getPrice()));
//        }
//        return storeProducts;
//    }
//
//
    /********************************************** Utilities ****************************************/
//
//    public StoreDataContainer getStoreDataById(String regionName,int storeId)
//    {
//        return regionsData.get(regionName).getStoresData().get(storeId);
//    }
//
//    public ProductDataContainer getProductDataById(int productId)
//    {
//        ProductDataContainer productDataContainer = null;
//        for (ProductDataContainer productData: getAllProductsData())
//        {
//            if (productData.getId() == productId)
//            {
//                productDataContainer = productData;
//                break;
//            }
//        }
//        return productDataContainer;
//    }
//
//    public UserDataContainer getCustomerDataById(int customerID)
//    {
//        UserDataContainer userDataContainer = null;
//        for (UserDataContainer customerData: getAllCustomersData())
//        {
//            if (customerData.getId() == customerID)
//            {
//                userDataContainer = customerData;
//                break;
//            }
//        }
//        return userDataContainer;
//    }
//
//    public float getDistanceBetweenStoreAndCustomer(StoreDataContainer store, UserDataContainer customer)
//    {
//        return Float.valueOf(DECIMAL_FORMAT.format(Math.abs((float) store.getPosition().distance(customer.getPosition()))));
//    }
//
//    public float getProductsCostFromStore(StoreDataContainer storeData, Collection<ProductDataContainer> products)
//    {
//        float cost = 0;
//        Store store = region.getStores().get(storeData.getId());
//        for (ProductDataContainer product:products)
//        {
//            cost += store.getProductById(product.getId()).getPrice() * product.amountProperty().get();
//        }
//
//        return  cost;
//    }
}
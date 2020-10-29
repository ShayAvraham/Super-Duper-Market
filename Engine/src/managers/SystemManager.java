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
import java.util.*;


public class SystemManager
{
    private DataManager dataManager;
    private Map <Integer, UserDataContainer> usersData;
    private Map <String, RegionDataContainer> regionsData;

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

    public StoreDataContainer GetStore(String regionName, int storeId)
    {
        return regionsData.get(regionName).getStoresData().get(storeId);
    }
    /**************** Load All Regions data **************/

    public Collection<RegionDataContainer> GetAllRegions()
    {
        return regionsData.values();
    }

    public Map<Integer, UserDataContainer> getUsersData() {
        return usersData;
    }

    public Map<String, RegionDataContainer> getRegionsData() {
        return regionsData;
    }


    /**************** Load All Users data **************/

    public Collection<UserDataContainer> GetAllUsers()
    {
        return usersData.values();
    }

    public UserDataContainer GetUserByID(int userId)
    {
        return usersData.get(userId);
    }


    /**************** Charge User Money **************/

    public synchronized void ChargeMoneyInUserAccount(int userId, float amountToCharge, Date transactionDate)
    {
        dataManager.ChargeMoneyInUserAccount(userId, amountToCharge, transactionDate);
        UserDataContainer userDataContainer = UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(userId));
        usersData.replace(userId, userDataContainer);
    }

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
                            Float deliveryCost, Float totalOrderCost, String regionName, int customerID)
    {
        OrderDataContainer newOrderData = new OrderDataContainer(
                deliveryDate,
                new Point(xPosition,yPosition),
                usersData.get(customerID).getName(),
                regionName,
                createOrderDataProducts(storesToBuyFrom,productsAmounts),
                createOrderDataDiscounts(selectedDiscounts),
                orderType.toLowerCase().equals("static")? false: true,
                productsCost,
                deliveryCost,
                totalOrderCost);

        Collection <User> usersCollection = dataManager.addNewOrder(newOrderData,regionName,customerID);
        updateDataContainers(regionName,customerID,usersCollection);
    }

    private synchronized void updateDataContainers(String regionName,Integer customerID, Collection<User> usersCollection)
    {
        int regionOwnerID = dataManager.getRegionOwnerID(regionName);
        Region region = dataManager.getAllRegions().get(regionName);
        regionsData.replace(regionName,RegionDataContainerBuilder.createRegionData(
                usersData.get(regionOwnerID).getName(),region));
        usersData.replace(customerID,UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(customerID)));
        usersData.replace(regionOwnerID,UserDataContainerBuilder.createUserData(dataManager.getAllUsers().get(regionOwnerID)));
        if(usersCollection != null)
        {
            for (User user : usersCollection)
            {
                usersData.replace(user.getId(), UserDataContainerBuilder.createUserData(user));
            }
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

    public boolean ValidatePosition(Integer xPosition, Integer yPosition,String regionName)
    {
        Point positionToValidate = new Point(xPosition,yPosition);
        for (StoreDataContainer store: regionsData.get(regionName).getStoresData().values())
        {
            if(store.getPosition().equals(positionToValidate))
            {
                return false;
            }
        }
        return true;
    }
    /********************************************** Show Store Orders ****************************************/

    public Collection<StoreDataContainer> GetOwnerStores(int ownerID, String regionName)
    {
        Collection<StoreDataContainer> ownerStores = new ArrayList<>();
        for (StoreDataContainer store : regionsData.get(regionName).getStoresData().values())
        {
            if(store.getOwnerName().equals(usersData.get(ownerID).getName()))
            {
                ownerStores.add(store);
            }
        }
        return ownerStores;
    }

    /********************************************** Show User Feedbacks ****************************************/

    public Collection<NoticeDataContainer> getUserRegionFeedbacks(String regionName, int userID)
    {
        Collection<NoticeDataContainer> userRegionFeedbacks = new ArrayList<>();
        for (NoticeDataContainer notice: usersData.get(userID).getNotices())
        {
            if(notice.getType().equals("feedback") && notice.getRegionName().equals(regionName))
            {
                userRegionFeedbacks.add(notice);
            }
        }
        return userRegionFeedbacks;
    }

    /********************************************** Add New Store ****************************************/


    public boolean ValidateStoreID(Integer storeID,String regionName)
    {
        for (StoreDataContainer store: regionsData.get(regionName).getStoresData().values())
        {
            if(store.getId() == (storeID))
            {
                return false;
            }
        }

        return true;
    }

    public void AddNewStore(String regionName, int ownerID, Integer storeID,
                            String storeName, Integer xPosition, Integer yPosition,
                            Integer storePPK, Collection<ProductDataContainer> selectedProducts)
    {
        Point location = new Point(xPosition,yPosition);
        StoreDataContainer newStoreData = new StoreDataContainer(storeID,
                storeName,
                usersData.get(ownerID).getName(),
                location,
                storePPK,
                selectedProducts);
        dataManager.addNewStore(newStoreData,regionName);
        updateDataContainers(regionName,ownerID,null);
    }
}
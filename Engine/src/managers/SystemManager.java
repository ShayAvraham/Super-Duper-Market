package managers;

import builders.UserDataContainerBuilder;
import dataContainers.ProductDataContainer;
import dataContainers.RegionDataContainer;
import dataContainers.StoreDataContainer;
import dataContainers.UserDataContainer;
import builders.RegionDataContainerBuilder;
import engineLogic.Owner;
import engineLogic.Region;
import engineLogic.User;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SystemManager
{
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE = "Unable to remove this product because its sold only in this store.";
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE = "Unable to remove this product because the store sold only this product.";


    private DataManager dataManager;
    private Map<Integer, UserDataContainer> usersData;
    private Map <String, RegionDataContainer> regionsData;

    private Map <StoreDataContainer,Collection<ProductDataContainer>> storeToPurchaseFrom;

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
        //AddNewUser(new UserDataContainer("dani","owner"));
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

    public Collection<ProductDataContainer> GetStoreProducts(String regionName, int storeId)//change
    {
        return regionsData.get(regionName).getStoresData().get(storeId).getProducts();
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
//
//    public void validateSelectedProducts(Collection<ProductDataContainer>selectedProducts)
//    {
//        if(selectedProducts.isEmpty())
//        {
//            throw new IllegalArgumentException("You must select at least one product");
//        }
//    }
//
//
    /** Dynamic Store Allocation **/
//    public Map<StoreDataContainer, Collection<ProductDataContainer>> dynamicStoreAllocation(Collection <ProductDataContainer> productsToPurchase)
//    {
//        storeToPurchaseFrom = new HashMap();
//        for (ProductDataContainer productToPurchase : productsToPurchase)
//        {
//            //   StoreDataContainer store = getStoreDataContainer(getStoreWithTheCheapestPrice(productToPurchase.getId()));
//            StoreDataContainer store = getStoreDataById(getStoreWithTheCheapestPrice(productToPurchase.getId()).getId());
//
//            ArrayList<ProductDataContainer> products = new ArrayList<ProductDataContainer>();
//            products.add(productToPurchase);
//            if(storeToPurchaseFrom.putIfAbsent(store, products) != null)
//            {
//                storeToPurchaseFrom.get(store).add(productToPurchase);
//            }
//        }
//        return storeToPurchaseFrom;
//
//    }
//
//    private Store getStoreWithTheCheapestPrice(int productId)
//    {
//        Store cheapestStore = null;
//        for(Store store : region.getStores().values())
//        {
//            StoreProduct product = store.getProductById(productId);
//            if(product != null)
//            {
//                if(cheapestStore == null)
//                {
//                    cheapestStore = store;
//                }
//                else if(cheapestStore.getProductById(productId).getPrice() > product.getPrice())
//                {
//                    cheapestStore = store;
//                }
//            }
//        }
//        return cheapestStore;
//    }
//
    /** Available Discounts **/
//
//    public Map<StoreDataContainer,Collection<DiscountDataContainer>> getAvailableDiscounts(Map<StoreDataContainer, Collection<ProductDataContainer>> storeToPurchaseFrom)
//    {
//        Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts = new HashMap<>();
//        for (StoreDataContainer store: storeToPurchaseFrom.keySet())
//        {
//            Collection <DiscountDataContainer> availableStoreDiscounts = getAvailableDiscountsInStore(store,storeToPurchaseFrom.get(store));
//            if(availableStoreDiscounts != null)
//            {
//                availableDiscounts.put(store,availableStoreDiscounts);
//            }
//        }
//        return availableDiscounts;
//    }
//
//    private Collection<DiscountDataContainer> getAvailableDiscountsInStore(StoreDataContainer store, Collection<ProductDataContainer> products)
//    {
//        Collection<DiscountDataContainer> availableStoreDiscounts = new ArrayList<>();
//        for(DiscountDataContainer discount : store.getDiscounts())
//        {
//            for (ProductDataContainer product : products)
//            {
//                if (discount.getDiscountProduct().equals(product))
//                {
//                    for(double i = product.amountProperty().get(); i >= discount.getAmountForDiscount(); i-=discount.getAmountForDiscount())
//                    {
//                        availableStoreDiscounts.add(createDiscountData(region.getStores().get
//                                (store.getId())
//                                .getDiscountByName(discount.getDiscountName())));
//                    }
//                }
//            }
//        }
//        if(availableStoreDiscounts.isEmpty())
//        {
//            return null;
//        }
//        return availableStoreDiscounts;
//    }
//
//    public void validateSelectedDiscounts(Collection<DiscountDataContainer> selectedDiscounts,Collection<ProductDataContainer> selectedProducts)
//    {
//        validatedOneOfDiscounts(selectedDiscounts);
//        validatedNumOfDiscounts(selectedDiscounts,selectedProducts);
//    }
//
//    private void validatedOneOfDiscounts(Collection<DiscountDataContainer> selectedDiscounts)
//    {
//        for (DiscountDataContainer discount:selectedDiscounts)
//        {
//            if(discount.getDiscountType().equals("ONE_OF") && discount.selectedOfferProductProperty().isNull().get())
//            {
//                throw new IllegalArgumentException(String.format("You must select a product if you want to get %1$s discount",discount.getDiscountName()));
//            }
//        }
//    }
//
//    private void validatedNumOfDiscounts(Collection<DiscountDataContainer> selectedDiscounts,Collection<ProductDataContainer> selectedProducts)
//    {
//        for(ProductDataContainer product: selectedProducts)
//        {
//            double sum = 0;
//            for (DiscountDataContainer discount : selectedDiscounts)
//            {
//                if(product.equals(discount.getDiscountProduct()))
//                {
//                    sum += discount.getAmountForDiscount();
//                }
//            }
//            if(sum>product.amountProperty().doubleValue())
//            {
//                throw new IllegalArgumentException(String.format("You chosen to many discounts for the product %1$s, please reselect the discounts",product.getName()));
//            }
//        }
//    }
//
    /** Create  New Order  **/
//
//    public Map<StoreDataContainer, Collection<DiscountDataContainer>> CreateOrderDataDiscounts(Map<StoreDataContainer, Collection<DiscountDataContainer>> selectedDiscounts)
//    {
//        Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts = new HashMap<>();
//        if(!selectedDiscounts.isEmpty())
//        {
//            for (StoreDataContainer store : selectedDiscounts.keySet())
//            {
//                discounts.put(store,createOrderDataStoreDiscounts(store,selectedDiscounts.get(store)));
//            }
//        }
//        return discounts;
//    }
//
//    private Collection<DiscountDataContainer> createOrderDataStoreDiscounts(StoreDataContainer store, Collection<DiscountDataContainer> discounts)
//    {
//        Collection<DiscountDataContainer> storeDiscounts = new ArrayList<>();
//        for (DiscountDataContainer discount : discounts)
//        {
//            if (discount.getDiscountType().equals("ONE_OF"))
//            {
//                storeDiscounts.add(createOrderDataStoreOneOfDiscount(discount));
//            }
//            else
//            {
//                storeDiscounts.add(discount);
//            }
//        }
//        return storeDiscounts;
//    }
//
//    private DiscountDataContainer createOrderDataStoreOneOfDiscount(DiscountDataContainer discount)
//    {
//        return new DiscountDataContainer(
//                discount.getDiscountName(),
//                discount.getDiscountType(),
//                discount.getDiscountProduct(),
//                discount.getAmountForDiscount(),
//                new HashMap<ProductDataContainer,Integer>(){{put(discount.selectedOfferProductProperty().get(),
//                        discount.getPriceForOfferProduct().get(discount.selectedOfferProductProperty().get()));}},
//                new HashMap<ProductDataContainer,Double>(){{put(discount.selectedOfferProductProperty().get(),
//                        discount.getAmountForOfferProduct().get(discount.selectedOfferProductProperty().get()));}});
//    }
//
//
//    public float getOrderCostOfAllProducts(Map <StoreDataContainer,Collection<ProductDataContainer>> products,
//                                           Map <StoreDataContainer,Collection<DiscountDataContainer>> discounts)
//    {
//        float orderCostOfAllProducts = 0;
//        for(StoreDataContainer store: products.keySet())
//        {
//            orderCostOfAllProducts += getStoreCostOfAllProducts(store,products.get(store),discounts.get(store));
//        }
//
//        return orderCostOfAllProducts;
//    }
//
//    private float getStoreCostOfAllProducts(StoreDataContainer store,Collection<ProductDataContainer> products,
//                                            Collection<DiscountDataContainer> discounts)
//    {
//        float storeCostOfAllProducts = 0;
//        storeCostOfAllProducts += getProductsCostFromStore(store,products);
//        if(discounts != null)
//        {
//            storeCostOfAllProducts += getDiscountsCostFromStore(store, discounts);
//        }
//        return storeCostOfAllProducts;
//    }
//
//    private float getDiscountsCostFromStore(StoreDataContainer store, Collection<DiscountDataContainer> discounts)
//    {
//        float costOfAllDiscounts = 0;
//        for (DiscountDataContainer discount : discounts)
//        {
//            for(ProductDataContainer product : discount.getPriceForOfferProduct().keySet())
//            {
//                costOfAllDiscounts += discount.getPriceForOfferProduct().get(product) *
//                        discount.getAmountForOfferProduct().get(product);
//            }
//        }
//        return costOfAllDiscounts;
//    }
//
//    public float getOrderDeliveryCost(Collection<StoreDataContainer> stores, UserDataContainer customer)
//    {
//        float deliveryCost = 0;
//        for(StoreDataContainer store : stores)
//        {
//            deliveryCost += getDeliveryCostFromStore(store,customer);
//        }
//        return deliveryCost;
//    }
//
//    public float getDeliveryCostFromStore(StoreDataContainer store, UserDataContainer customer)
//    {
//        float deliveryCost  = getDistanceBetweenStoreAndCustomer(store, customer) * store.getPpk();
//        return Float.valueOf(DECIMAL_FORMAT.format(deliveryCost));
//    }
//
//
    /**     Add New Order    **/
//    public void addNewOrder(OrderDataContainer newOrderDataContainer)
//    {
//        Map <Integer,Order> newSubOrders = new HashMap<>();
//        Order newOrder = createNewOrder(newOrderDataContainer);
//        newSubOrders = createSubOrders(newOrderDataContainer, newOrder.getId());
//
//        region.addNewOrder(newOrder,newSubOrders);
//        storeToPurchaseFrom = null;
//        updateDataContainers();
//    }
//
//
//    private Order createNewOrder(OrderDataContainer newOrderDataContainer)
//    {
//        return new Order(newOrderDataContainer.getDate(),
//                region.getCustomers().get(newOrderDataContainer.getCustomer().getId()),
//                createOrderProducts(newOrderDataContainer.getProducts()),
//                createOrderDiscounts(newOrderDataContainer.getDiscounts()),
//                newOrderDataContainer.isDynamic(),
//                newOrderDataContainer.getCostOfAllProducts(),
//                newOrderDataContainer.getDeliveryCost(),
//                newOrderDataContainer.getTotalCost());
//    }
//
//    private Map<Store, Collection<OrderProduct>> createOrderProducts(Map<StoreDataContainer, Collection<ProductDataContainer>> products)
//    {
//        Map<Store, Collection<OrderProduct>> orderStoreAndProducts = new HashMap<>();
//        for(StoreDataContainer store: products.keySet())
//        {
//            Store orderStore = region.getStores().get(store.getId());
//            orderStoreAndProducts.put(orderStore,createOrderStoreProducts(store,products.get(store)));
//        }
//        return orderStoreAndProducts;
//    }
//
//    private Collection<OrderProduct> createOrderStoreProducts(StoreDataContainer store, Collection<ProductDataContainer> products)
//    {
//        Collection<OrderProduct> orderStoreProducts = new ArrayList<>();
//        for(ProductDataContainer product : products)
//        {
//            orderStoreProducts.add(new OrderProduct(
//                    region.getStores().get(store.getId()).getProductById(product.getId()),
//                    new Float(product.getAmount())));
//        }
//        return orderStoreProducts;
//    }
//
//    private Map<Store, Collection<Discount>> createOrderDiscounts(Map<StoreDataContainer, Collection<DiscountDataContainer>> discounts)
//    {
//        Map<Store, Collection<Discount>> orderStoreAndProducts = new HashMap<>();
//        if(!discounts.isEmpty())
//        {
//            for (StoreDataContainer store : discounts.keySet())
//            {
//                Store orderStore = region.getStores().get(store.getId());
//                orderStoreAndProducts.put(orderStore, createOrderStoreDiscounts(store, discounts.get(store)));
//            }
//        }
//        return orderStoreAndProducts;
//    }
//
//    private Collection<Discount> createOrderStoreDiscounts(StoreDataContainer store, Collection<DiscountDataContainer> discounts)
//    {
//        Collection<Discount> orderStoreDiscounts = new ArrayList<>();
//        for(DiscountDataContainer discount : discounts)
//        {
//            orderStoreDiscounts.add(new Discount(discount.getDiscountName(),
//                    discount.getDiscountType(),
//                    createDiscountProduct(store,discount),
//                    createProductsToOffer(store ,discount)));
//        }
//        return orderStoreDiscounts;
//    }
//
//    private DiscountProduct createDiscountProduct(StoreDataContainer store, DiscountDataContainer discount)
//    {
//        return new DiscountProduct( region.getStores().get(store.getId()).getProductById(discount.getDiscountProduct().getId()),
//                discount.getAmountForDiscount());
//    }
//
//    private Collection<OfferProduct> createProductsToOffer(StoreDataContainer store,DiscountDataContainer discount)
//    {
//        Collection<OfferProduct> offerProducts = new ArrayList<>();
//        for (ProductDataContainer product : discount.getPriceForOfferProduct().keySet())
//        {
//            offerProducts.add(new OfferProduct(region.getStores().get(store.getId()).getProductById(product.getId()),
//                    discount.getPriceForOfferProduct().get(product),
//                    discount.getAmountForOfferProduct().get(product)));
//        }
//        return offerProducts;
//    }
//
//    private Map<Integer,Order> createSubOrders(OrderDataContainer newOrderDataContainer,int orderID)
//    {
//        Map <Integer,Order> subOrders = new HashMap<>();
//        for(StoreDataContainer store : newOrderDataContainer.getProducts().keySet())
//        {
//            subOrders.put(store.getId(),createSubOrder(newOrderDataContainer,orderID ,store));
//        }
//        return subOrders;
//    }
//
//    private Order createSubOrder(OrderDataContainer newOrder, int orderID, StoreDataContainer storeData)
//    {
//        Store store = region.getStores().get(storeData.getId());
//        float costOfAllProducts = getStoreCostOfAllProducts(storeData,newOrder.getProducts().get(storeData),
//                newOrder.getDiscounts().get(storeData));
//        float deliveryCost = getDeliveryCostFromStore(storeData,newOrder.getCustomer());
//
//        return new Order(orderID,
//                newOrder.getDate(),
//                region.getCustomers().get(newOrder.getCustomer().getId()),
//                new HashMap<Store,Collection<OrderProduct>>()
//                {{put(store,createOrderStoreProducts(storeData,newOrder.getProducts().get(storeData)));}},
//                createSubOrderDiscounts(newOrder,storeData,store),
//                newOrder.isDynamic(),
//                costOfAllProducts,
//                deliveryCost,
//                costOfAllProducts + deliveryCost);
//    }
//
//    private HashMap<Store,Collection<Discount>> createSubOrderDiscounts(OrderDataContainer newOrder,StoreDataContainer storeData,
//                                                                        Store store)
//    {
//        HashMap<Store,Collection<Discount>> discounts = new HashMap<Store,Collection<Discount>>();
//        if(newOrder.getDiscounts().get(storeData) != null)
//        {
//            discounts.put(store, createOrderStoreDiscounts(storeData, newOrder.getDiscounts().get(storeData)));
//        }
//        return discounts;
//    }
//
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
//    public StoreDataContainer getStoreDataById(int storeId)
//    {
//        StoreDataContainer storeDataContainer = null;
//        for (StoreDataContainer storeData : allStoresData)
//        {
//            if(storeData.getId() == storeId)
//            {
//                storeDataContainer = storeData;
//                break;
//            }
//        }
//        return storeDataContainer;
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
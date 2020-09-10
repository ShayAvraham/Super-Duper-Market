package engineLogic;
import dataContainers.*;
import exceptions.DiscountRemoveException;
import javafx.concurrent.Task;
import jaxb.generated.ThenYouGet;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.io.Console;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;


public class SystemManager
{
    private final String IF_YOU_BUY_MSG = "If you buy The %1$s %2$s of %3$s";
    private final String THEN_YOU_GET_MSG = "then you get %1$s";
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE = "Unable to remove this product because its sold only in one store.";
    private final String UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE = "Unable to remove this product because the store sold only this product.";

    private SystemData systemData;
    private XmlSystemDataBuilder xmlSystemDataBuilder;
    private Collection<StoreDataContainer> allStoresData;
    private Collection<ProductDataContainer> allProductsData;
    private Collection<OrderDataContainer> allOrdersData;
    private Collection<CustomerDataContainer> allCustomersData;
    private boolean isFileWasLoadSuccessfully = false;
    private Map <ProductDataContainer, StoreDataContainer> storesToBuyFrom;
    private Map <Integer,Float> deliveryCostFromStores;

    public SystemManager()
    {
        this.xmlSystemDataBuilder = new XmlSystemDataBuilder();
    }

    public Collection<StoreDataContainer> getAllStoresData() {
        return allStoresData;
    }

    public Collection<ProductDataContainer> getAllProductsData() {
        return allProductsData;
    }

    public Collection<OrderDataContainer> getAllOrdersData() {
        return allOrdersData;
    }

    public Collection<CustomerDataContainer> getAllCustomersData() {
        return allCustomersData;
    }

    public boolean isFileWasLoadSuccessfully() {
        return isFileWasLoadSuccessfully;
    }

    public void loadDataFromXmlFile(String xmlFilePath) throws JAXBException, FileNotFoundException, InstanceNotFoundException
    {
        SystemData newSystemData = xmlSystemDataBuilder.deserializeXmlToSystemData(xmlFilePath);
        systemData = newSystemData;
        updateDataContainers();
        isFileWasLoadSuccessfully = true;
    }

    public void addNewOrder(OrderDataContainer newOrderDataContainer)
    {
        Order newOrder;
        Map <Integer,Order> newSubOrders = new HashMap<>();
        if(!newOrderDataContainer.isDynamic())
        {
            newOrder = createNewStaticOrder(newOrderDataContainer);
            newSubOrders.put(newOrder.getStoreId(), newOrder);
        }
        else
        {
            newOrder = createNewDynamicOrder(newOrderDataContainer);
            newSubOrders = createSubOrders(newOrder);
        }

        systemData.addNewOrder(newOrder,newSubOrders);
        storesToBuyFrom = null;
        deliveryCostFromStores = null;
        updateDataContainers();
    }

    private Order createNewStaticOrder(OrderDataContainer newOrderDataContainer)
    {
        return new Order(
                newOrderDataContainer.getStoreId(),
                newOrderDataContainer.getDate(),
                newOrderDataContainer.getDeliveryCost(),
                createOrderProductsFromOrderData(newOrderDataContainer));
    }

    private Order createNewDynamicOrder(OrderDataContainer newOrderDataContainer)
    {
        return new Order(
                newOrderDataContainer.getDate(),
                newOrderDataContainer.getDeliveryCost(),
                newOrderDataContainer.getNumberOfStoresOrderedFrom(),
                createOrderProductsFromOrderData(newOrderDataContainer));
    }

    private Collection<OrderProduct> createOrderProductsFromOrderData(OrderDataContainer newOrderDataContainer)
    {
        Collection<OrderProduct> orderProducts  = new ArrayList<>();
        for(Integer productId : newOrderDataContainer.getAmountPerProduct().keySet())
        {
            int storeId = newOrderDataContainer.isDynamic()?
                    storesToBuyFrom.get(getProductDataById(productId)).getId():
                    newOrderDataContainer.getStoreId();

            orderProducts.add(new OrderProduct(
                    systemData.getStores().get(storeId).getProductById(productId),
                    newOrderDataContainer.getAmountPerProduct().get(productId)));
        }
        return orderProducts;
    }

    private Map<Integer,Order> createSubOrders(Order newOrder)
    {
        Map <Integer,Order> subOrders = new HashMap<>();
        for(ProductDataContainer product : storesToBuyFrom.keySet())
        {
            int storeId = storesToBuyFrom.get(product).getId();
            Order subOrder = createSubOrder(newOrder, storeId, product.getId());
            Order orderSucceed = subOrders.putIfAbsent(storeId,subOrder);
            if(orderSucceed != null)
            {
                subOrders.get(storeId).addOrderProduct(subOrder.getOrderedProducts().stream().findFirst().get());
            }
        }
        return subOrders;
    }

    private Order createSubOrder(Order newOrder, int storeId, int productId)
    {
        Collection<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(new OrderProduct(
                systemData.getStores().get(storeId).getProductById(productId),
                newOrder.getProductAmountInOrder(systemData.getProducts().get(productId))));

        return new Order(
                newOrder.getId(),
                storeId,
                newOrder.getOrderDate(),
                deliveryCostFromStores.get(storeId),
                orderProducts);
    }

    private void updateDataContainers()
    {
        createAllStoresData();
        createAllProductsData();
        createAllOrdersData();
        createAllCustomersData();
    }


    private void createAllStoresData()
    {
        allStoresData = new ArrayList<>();
        for (Store store: systemData.getStores().values())
        {
            allStoresData.add(new StoreDataContainer(
                    store.getId(),
                    store.getName(),
                    store.getPosition(),
                    store.getPPK(),
                    getStoreTotalIncomeFromDeliveries(store),
                    getStoreProductsData(store),
                    getStoreOrdersData(store),
                    getStoreDiscountsData(store)));
        }
    }

    private float getStoreTotalIncomeFromDeliveries(Store store)
    {
        float totalIncomeFromDeliveries = 0;

        for (Order order: store.getStoreOrders())
        {
            totalIncomeFromDeliveries += order.getDeliveryCost();
        }
        return totalIncomeFromDeliveries;
    }

    private Collection<ProductDataContainer> getStoreProductsData(Store store)
    {
        Collection<ProductDataContainer> allProductsData = new ArrayList<>();
        for (StoreProduct product: store.getStoreProducts())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm().name(),
                    getProductPricePerStore(product),
                    getSoldAmountPerStore(product)));
        }

        return allProductsData;
    }


    private Map<Integer, Integer> getProductPricePerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Integer> productPricePerStore = new HashMap<>();
        for (Store store: systemData.getStores().values())
        {
            for (StoreProduct storeProduct: store.getStoreProducts())
            {
                if (selectedProduct.equals(storeProduct))
                {
                    productPricePerStore.put(store.getId(), storeProduct.getPrice());
                    break;
                }
            }
        }
        return productPricePerStore;
    }

    private Map<Integer, Float> getSoldAmountPerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Float> soldAmountPerStore = new HashMap<>();
        float soldAmount;
        for(Store store: systemData.getStores().values())
        {
            soldAmount = store.getHowManyTimesProductSold(selectedProduct);
            soldAmountPerStore.put(store.getId(),soldAmount);
        }
        return soldAmountPerStore;
    }

    private Collection<OrderDataContainer> getStoreOrdersData(Store store)
    {
        Collection<OrderDataContainer> allOrdersData = new ArrayList<>();
        for (Order order: store.getStoreOrders())
        {
            allOrdersData.add(createStoreOrderData(order));
        }

        return allOrdersData;
    }

    private OrderDataContainer createStoreOrderData(Order order)
    {
        return new OrderDataContainer(
                order.getOrderDate(),
                order.getAllOrderedProductsQuantity(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCostOfOrder());
    }


    private Collection<DiscountDataContainer> getStoreDiscountsData(Store store)
    {
        Collection<DiscountDataContainer> allDiscountData = new ArrayList<>();
        for (Discount discount: store.getStoreDiscounts())
        {
            allDiscountData.add(createStoreDiscountData(discount));
        }

        return allDiscountData;
    }

    private DiscountDataContainer createStoreDiscountData(Discount discount)
    {
        return new DiscountDataContainer(discount.getName(),
                                        createIfYouBuyDescription(discount.getDiscountProduct()),
                                        createThenYouGetDescription(discount.getProductsToOffer(),discount.getDiscountType()));
    }

    private String createIfYouBuyDescription(DiscountProduct discountProduct)
    {
        String purchaseFormStr = createPurchaseFormStr(discountProduct.getPurchaseForm(),discountProduct.getAmountForDiscount());
        return String.format(IF_YOU_BUY_MSG,discountProduct.getAmountForDiscount(),purchaseFormStr,discountProduct.getName());
    }

    private String createPurchaseFormStr(Product.ProductPurchaseForm purchaseForm,double amount)
    {
        String purchaseFormStr = "";
        switch (purchaseForm)
        {
            case WEIGHT:
                purchaseFormStr = "kg";
                break;
            case QUANTITY:
                purchaseFormStr = "unit";
                if(amount > 1)
                {
                    purchaseFormStr += "s";
                }
                break;
        }
        return purchaseFormStr;
    }

    private String createThenYouGetDescription(Collection<OfferProduct> offerProducts, Discount.DiscountType discountType)
    {
        String productsToGet = THEN_YOU_GET_MSG;
        String discountTypeStr = createDiscountTypeStr(discountType);

        for(OfferProduct product: offerProducts)
        {
            String purchaseFormStr = createPurchaseFormStr(product.getPurchaseForm(),product.getOfferAmount());
            if(!product.equals(offerProducts.stream().findFirst()))
            {
                productsToGet += discountTypeStr;
            }
            productsToGet += String.format(" %1$s %2$s of %3$s for %4$s",
                                            product.getOfferAmount(),
                                            purchaseFormStr,
                                            product.getName(),
                                            product.getOfferPrice());
        }

        return String.format(THEN_YOU_GET_MSG, productsToGet);

    }

    private String createDiscountTypeStr(Discount.DiscountType discountType)
    {
        String discountTypeStr = "";
        switch (discountType)
        {
            case ONE_OF:
                discountTypeStr = " or";
                break;
            case ALL_OR_NOTHING:
                discountTypeStr = " and";
                break;
        }
        return discountTypeStr;
    }

    private void createAllProductsData()
    {
        allProductsData = new ArrayList<>();
        for (Product product: systemData.getProducts().values())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm().name(),
                    getHowManyStoresSellProduct(product),
                    getProductAvgPrice(product),
                    getHowManyTimesProductSold(product)));
        }
    }

    private int getHowManyStoresSellProduct(Product product)
    {
        int howManyStoresSellProduct = 0;

        for (Store store: systemData.getStores().values())
        {
            if (store.isProductInStore(product))
            {
                howManyStoresSellProduct++;
            }
        }
        return howManyStoresSellProduct;
    }

    private float getProductAvgPrice(Product selectedProduct)
    {
        float sumOfProductPrices = 0;
        int numOfStoresWhoSellsProduct = getHowManyStoresSellProduct(selectedProduct);

        for (Store store: systemData.getStores().values())
        {
            StoreProduct product = store.getProductById(selectedProduct.getId());
            if (product != null)
            {
                sumOfProductPrices += product.getPrice();
            }
        }
        return (sumOfProductPrices/numOfStoresWhoSellsProduct);
    }

    private float getHowManyTimesProductSold(Product product)
    {
        float howManyTimesProductSold = 0;

        for (Store store: systemData.getStores().values())
        {
            howManyTimesProductSold += store.getHowManyTimesProductSold(product);
        }
        return howManyTimesProductSold;
    }


    private void createAllOrdersData()
    {
        allOrdersData = new ArrayList<>();
        for (Order order: systemData.getOrders())
        {
            allOrdersData.add(createOrderData(order));
        }
    }

    private OrderDataContainer createOrderData(Order order)
    {
        String storeName = order.isDynamic()?
                "Multy store order":
                systemData.getStores().get(order.getStoreId()).getName();

        return new OrderDataContainer(
                order.getId(),
                order.getOrderDate(),
                order.getStoreId(),
                storeName,
                order.getOrderedProducts().size(),
                order.getAllOrderedProductsQuantity(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCostOfOrder(),
                order.isDynamic(),
                order.getNumberOfStoresOrderedFrom());
    }

    private void createAllCustomersData()
    {
        allCustomersData = new ArrayList<>();
        for (Customer customer: systemData.getCustomers().values())
        {
            allCustomersData.add(createCustomerData(customer));
        }
    }

    private CustomerDataContainer createCustomerData(Customer customer)
    {
        return new CustomerDataContainer(
                customer.getId(),
                customer.getName(),
                getCustomerNumOfOrders(customer),
                getCustomerOrderCostAvg(customer),
                getCustomerDeliveryCostAvg(customer),
                customer.getPosition());
    }

    private int getCustomerNumOfOrders(Customer customer)
    {
        int numOfOrders = 0;
        for(Order order: systemData.getOrders())
        {
            if(order.getCustomer().equals(customer))
            {
                numOfOrders++;
            }
        }
        return numOfOrders;
    }

    private float getCustomerOrderCostAvg(Customer customer)
    {
//        float orderCostSum = 0;
//        int numOfOrders = 0;
//        for(Order order: systemData.getOrders())
//        {
//            if(order.getCustomer().equals(customer))
//            {
//                orderCostSum += order.getCostOfAllProducts();
//                numOfOrders++;
//            }
//        }
    //    return orderCostSum/numOfOrders;

        return (float) systemData.getOrders().stream()
                .filter(order -> order.getCustomer().equals(customer))
                .mapToDouble(Order::getCostOfAllProducts)
                .average()
                .orElse(0.0);
    }

    private float getCustomerDeliveryCostAvg(Customer customer)
    {
//        int deliveryCostSum = 0;
//        int numOfOrders = 0;
//        for(Order order: systemData.getOrders())
//        {
//            if(order.getCustomer().equals(customer))
//            {
//                deliveryCostSum += order.getDeliveryCost();
//                numOfOrders++;
//            }
//        }
//        return deliveryCostSum/numOfOrders;

        return (float) systemData.getOrders().stream()
                .filter(order -> order.getCustomer().equals(customer))
                .mapToDouble(Order::getDeliveryCost)
                .average()
                .orElse(0.0);
    }

    public void removeProductFromStore(StoreDataContainer store, ProductDataContainer productToRemove) throws ValidationException
    {
        validateRemoveProduct(store,productToRemove);
        try
        {
             systemData.removeProductFromStore(store.getId(),productToRemove.getId());
        }
        finally
        {
            updateDataContainers();
        }



    }

    private void validateRemoveProduct(StoreDataContainer store, ProductDataContainer product) throws ValidationException
    {
        if(!isOthersStoresSellThisProduct(store, product))
        {
            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_STORE_MESSAGE);
        }
        if(store.getProducts().size() == 1)
        {
            throw new ValidationException(UNABLE_TO_REMOVE_PRODUCT_ONE_PRODUCT_MESSAGE);
        }
    }

    private boolean isOthersStoresSellThisProduct(StoreDataContainer selectedStore, ProductDataContainer product)
    {
        for(StoreDataContainer store: getAllStoresData())
        {
            if(!selectedStore.equals(store))
            {
                if(store.getProducts().contains(product))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void addProductToStore(StoreDataContainer store, ProductDataContainer productToAdd, int price)
    {
        systemData.addProductToStore(store.getId(),productToAdd.getId(),price);
        updateDataContainers();
    }

    public void updateProductPriceInStore(StoreDataContainer store, ProductDataContainer productToRemove, int newPrice)
    {
        systemData.updateProductPriceInStore(store.getId(), productToRemove.getId(), newPrice);
        updateDataContainers();
    }

    public Map<ProductDataContainer, StoreDataContainer> dynamicStoreAllocation(Collection <ProductDataContainer> productsToPurchase)
    {
        storesToBuyFrom = new HashMap<>();
        for (ProductDataContainer productToPurchase : productsToPurchase)
        {
            Store store = getStoreWithTheCheapestPrice(productToPurchase.getId());
            storesToBuyFrom.put(productToPurchase,getStoreDataContainer(store));
        }
        return storesToBuyFrom;
    }

    private Store getStoreWithTheCheapestPrice(int productId)
    {
        Store cheapestStore = null;
        for(Store store : systemData.getStores().values())
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

    private StoreDataContainer getStoreDataContainer(Store store)
    {
        StoreDataContainer storeDataContainer = null;
        for (StoreDataContainer storeData : allStoresData)
        {
            if(storeData.getId() == store.getId())
            {
                storeDataContainer = storeData;
                break;
            }
        }
        return storeDataContainer;
    }

    public Set<Integer> getAllStoresID()
    {
        Set<Integer> allStoresID = new HashSet<>();

        for (StoreDataContainer storeData: getAllStoresData())
        {
            allStoresID.add(storeData.getId());
        }
        return allStoresID;
    }

    public Set<Integer> getAllProductsID() {
        Set<Integer> allProductsID = new HashSet<>();

        for (ProductDataContainer productData : getAllProductsData()) {
            allProductsID.add(productData.getId());
        }
        return allProductsID;
    }

    public StoreDataContainer getStoreDataById(int storeId)
    {
        StoreDataContainer storeDataContainer = null;
        for (StoreDataContainer storeData : allStoresData)
        {
            if(storeData.getId() == storeId)
            {
                storeDataContainer = storeData;
                break;
            }
        }
        return storeDataContainer;
    }

    public ProductDataContainer getProductDataById(int productId)
    {
        ProductDataContainer productDataContainer = null;
        for (ProductDataContainer productData: getAllProductsData())
        {
            if (productData.getId() == productId)
            {
                productDataContainer = productData;
                break;
            }
        }
        return productDataContainer;
    }

    public float getDeliveryCost(Point userLocation, Collection<StoreDataContainer> storesParticapatesInOrder)
    {
        float deliveryCost = 0;
        deliveryCostFromStores = new HashMap<>();
        for (StoreDataContainer storeData: storesParticapatesInOrder)
        {
            for (Store store: systemData.getStores().values())
            {
                if (storeData.getId() == store.getId())
                {
                    deliveryCost += store.getDeliveryCostByLocation(userLocation);
                    deliveryCostFromStores.put(store.getId(),store.getDeliveryCostByLocation(userLocation));
                }
            }
        }
        return deliveryCost;
    }

    public float getDistanceBetweenStoreAndCustomer(Point userLocation, StoreDataContainer store)
    {
        return systemData.getStores().get(store.getId()).getDistanceToCustomer(userLocation);
    }
}
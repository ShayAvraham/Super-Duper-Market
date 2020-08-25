import exceptions.StoreDoesNotSellProductException;
import exceptions.UserLocationEqualToStoreException;
import exceptions.UserLocationNotValidException;

import javax.management.InstanceNotFoundException;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.CollationElementIterator;
import java.text.DecimalFormat;
import java.util.*;
import java.util.jar.JarException;

public class SystemManager
{
    private SystemData systemData;
    private XmlSystemDataBuilder xmlSystemDataBuilder;
    private Collection<StoreDataContainer> allStoresData;
    private Collection<ProductDataContainer> allProductsData;
    private Collection<OrderDataContainer> allOrdersData;
    private boolean isFileWasLoadSuccessfully = false;
    private Map <ProductDataContainer,StoreDataContainer> storesToBuyFrom;
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
                    getStoreOrdersData(store)));
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
        for (StoreProduct product: store.getProductsInStore())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm(),
                    getProductPricePerStore(product),
                    getSoldAmountPerStore(product)));
        }

        return allProductsData;
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

    private Map<Integer, Integer> getProductPricePerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Integer> productPricePerStore = new HashMap<>();
        for (Store store: systemData.getStores().values())
        {
            for (StoreProduct storeProduct: store.getProductsInStore())
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

    private void createAllProductsData()
    {
        allProductsData = new ArrayList<>();
        for (Product product: systemData.getProducts().values())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm(),
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

    public void removeProductFromStore(StoreDataContainer store, ProductDataContainer productToRemove)
    {
        systemData.removeProductFromStore(store.getId(),productToRemove.getId());
        updateDataContainers();
    }

    public void addProductToStore(StoreDataContainer store, ProductDataContainer productToAdd, int price)
    {
        systemData.addProductToStore(store.getId(),productToAdd.getId(),price);
        updateDataContainers();
    }

    public void updateProductPriceInStore(StoreDataContainer store,ProductDataContainer productToRemove, int newPrice)
    {
        systemData.updateProductPriceInStore(store.getId(), productToRemove.getId(), newPrice);
        updateDataContainers();
    }

    public Map<ProductDataContainer,StoreDataContainer> dynamicStoreAllocation(Collection <ProductDataContainer> productsToPurchase)
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
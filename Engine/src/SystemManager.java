import exceptions.StoreDoesNotSellProductException;
import exceptions.UserLocationEqualToStoreException;
import exceptions.UserLocationNotValidException;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.CollationElementIterator;
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

    public void loadDataFromXmlFile(String xmlFilePath) throws JAXBException, FileNotFoundException
    {
        SystemData newSystemData = xmlSystemDataBuilder.deserializeXmlToSystemData(xmlFilePath);
        systemData = newSystemData;
        updateDataContainers();
        isFileWasLoadSuccessfully = true;
    }

    public void addNewOrder(OrderDataContainer newOrderDataContainer)
    {
        Order newOrder = new Order(
                newOrderDataContainer.getStoreId(),
                newOrderDataContainer.getDate(),
                newOrderDataContainer.getDeliveryCost(),
                createOrderProductsFromOrderData(newOrderDataContainer));

        systemData.addNewOrder(newOrder);
        updateDataContainers();


    }

    private Collection<OrderProduct> createOrderProductsFromOrderData(OrderDataContainer newOrderDataContainer)
    {
        Collection<OrderProduct> orderProducts  = new ArrayList<>();
        for(Integer productId : newOrderDataContainer.getAmountPerProduct().keySet())
        {
            orderProducts.add(new OrderProduct(
                    systemData.getStores().get(newOrderDataContainer.getStoreId()).getProductById(productId),
                    newOrderDataContainer.getAmountPerProduct().get(productId)));
        }
        return orderProducts;
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
            totalIncomeFromDeliveries += order.getTotalCostOfOrder();
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
        return new OrderDataContainer(
                order.getId(),
                order.getOrderDate(),
                order.getStoreId(),
                systemData.getStores().get(order.getStoreId()).getName(),
                order.getOrderedProducts().size(),
                order.getAllOrderedProductsQuantity(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCostOfOrder());
    }

//bonus add/remove/set price product

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


//bonus

//bonus

    public Collection<StoreDataContainer> dynamicStoreAllocation(Collection <ProductDataContainer> productsToPurchase)
    {
        Collection<StoreDataContainer> storeToBuyFrom = new ArrayList<>();

        for (ProductDataContainer productToPurchase : productsToPurchase)
        {

        }
        return storeToBuyFrom;
    }

//bonus

//    public int getTotalAmountOfProductsInOrder(Order order)
//    {
//        int totalAmountOfProducts = 0;
//
//        for (OrderProduct product: order.getOrderedProducts())
//        {
//            totalAmountOfProducts += product.getAmount();
//        }
//        return totalAmountOfProducts;
//    }

//    public float getTotalCostOfAllProductsInOrder(Order order)
//    {
//        float totalCostOfAllProducts = 0;
//
//        for (OrderProduct product: order.getProductsInOrder())
//        {
//            totalCostOfAllProducts += product.getAmount() * product.getStoreProduct().getPrice();
//        }
//        return totalCostOfAllProducts;
//    }

//    public float getTotalCostOfOrder(Order order)
//    {
//        return getTotalCostOfAllProductsInOrder(order) + order.getDeliveryCost();
//    }


//    public void checkIsUserLocationValid(Point userLocation, int storeId)
//    {
//        if (userLocation.getX() < 1 || userLocation.getX() > 50 ||
//                userLocation.getY() < 1 || userLocation.getY() > 50)
//        {
//            throw new UserLocationNotValidException(userLocation);
//        }
//
//        Point storeLocation = getStoreById(storeId).getPosition();
//        if (userLocation.equals(storeLocation))
//        {
//            throw new UserLocationEqualToStoreException(userLocation, storeLocation);
//        }
//    }
//
//    public Set<Integer> getAllStoresID()
//    {
//        Set<Integer> allStoresID = new HashSet<>();
//
//        for (Store store: getAllStores())
//        {
//            allStoresID.add(store.getId());
//        }
//        return allStoresID;
//    }
//
//    public Set<Integer> getAllProductsID()
//    {
//        Set<Integer> allProductsID = new HashSet<>();
//
//        for (Product product: getAllProducts())
//        {
//            allProductsID.add(product.getId());
//        }
//        return allProductsID;
//    }
//
//    public Product getProductById(int productId)
//    {
//        Product product = null;
//
//        for (Product currentProduct: getAllProducts())
//        {
//            if (currentProduct.getId() == productId)
//            {
//                product = currentProduct;
//                break;
//            }
//        }
//        return product;
//    }
//
//    public void checkIsStoreSellProduct(int productId, int storeId)
//    {
//        Store store = getStoreById(storeId);
//        if (store.getProductById(productId) == null)
//        {
//            throw new StoreDoesNotSellProductException();
//        }
//    }
}

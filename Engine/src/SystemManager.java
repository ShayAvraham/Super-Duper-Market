import exceptions.StoreDoesNotSellProductException;
import exceptions.UserLocationEqualToStoreException;
import exceptions.UserLocationNotValidException;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.jar.JarException;

public class SystemManager
{
    private SystemData systemData;
    private XmlSystemDataBuilder xmlSystemDataBuilder;
    private boolean isFileWasLoadSuccessfully = false;

    public SystemManager()
    {
        this.xmlSystemDataBuilder = new XmlSystemDataBuilder();
    }

    public void loadDataFromXmlFile(String xmlFilePath) throws JAXBException, FileNotFoundException
    {
        SystemData newSystemData = xmlSystemDataBuilder.deserializeXmlToSystemData(xmlFilePath);
        isFileWasLoadSuccessfully = true;
        systemData = newSystemData;
    }

    public boolean isFileWasLoadSuccessfully()
    {
        return isFileWasLoadSuccessfully;
    }

    public Collection<StoreDataContainer> getAllStoresData()
    {
        Collection<StoreDataContainer> allStoresData = new ArrayList<>();
        for (Store store: systemData.getStores().values())
        {
            allStoresData.add(new StoreDataContainer(
                   store.getId(),
                   store.getName(),
                   store.getPpk(),
                   getStoreTotalCashFromDeliveries(store),
                    getStoreProductsData(store),
                    getStoreOrdersData(store)));
        }
        return allStoresData;
    }

    public float getStoreTotalCashFromDeliveries(Store store)
    {
        float totalCashFromDeliveries = 0;

        for (Order order: store.getStoreOrders())
        {
            totalCashFromDeliveries += order.getDeliveryCost();
        }
        return totalCashFromDeliveries;
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
            allOrdersData.add(new OrderDataContainer(
                    order.getId(),
                    order.getOrderDate(),
                    order.getAllOrderedProductsQuantity(),
                    order.getCostOfAllProducts(),
                    order.getDeliveryCost(),
                    order.getTotalCostOfOrder()));
        }

        return allOrdersData;
    }

    private Map<Integer, Float> getProductPricePerStore(StoreProduct selectedProduct)
    {
        Map<Integer,Float> productPricePerStore = new HashMap<>();
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

    public Collection<ProductDataContainer> getAllProductsData()
    {
        Collection<ProductDataContainer> allProductsData = new ArrayList<>();
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
        return allProductsData;
    }

    public int getHowManyStoresSellProduct(Product product)
    {
        int howManyStoresSellProduct = 0;

        for (Store store: systemData.getStores().values())
        {
            if (store.getHowManyTimesProductSold(product) > 0)
            {
                howManyStoresSellProduct++;
            }
        }
        return howManyStoresSellProduct;
    }

    public float getProductAvgPrice(Product selectedProduct)
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

    public int getHowManyTimesProductSold(Product product)
    {
        int howManyTimesProductSold = 0;

        for (Store store: systemData.getStores().values())
        {
            howManyTimesProductSold += store.getHowManyTimesProductSold(product);
        }
        return howManyTimesProductSold;
    }

    //
//    public Collection<OrderDataContainer> getAllOrdersData()
//    {
//        return null;
//    }




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

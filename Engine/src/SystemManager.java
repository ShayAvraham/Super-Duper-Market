import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SystemManager
{
    private SystemData systemData;

    public SystemManager(SystemData systemData)
    {
        this.systemData = systemData;
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

    public int getHowManyTimesProductSold(Product product)
    {
        int howManyTimesProductSold = 0;

        for (Store store: systemData.getStores().values())
        {
            howManyTimesProductSold += getHowManyTimesProductSoldBySpecificStore(store, product);
        }
        return howManyTimesProductSold;
    }

    public int getHowManyTimesProductSoldBySpecificStore(Store store, Product product)
    {
        return store.getHowManyTimesProductSold(product);
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


    public Collection<Order> getAllOrders(Store store)
    {

        Collection<Order> allOrders = null;
        for (Store currentStore: systemData.getStores().values())
        {
            if(currentStore.getId() == store.getId())
            {
                allOrders = currentStore.getStoreOrders();
                break;
            }
        }
        return allOrders;
    }

    public int getTotalAmountOfProductsInOrder(Order order)
    {
        int totalAmountOfProducts = 0;

        for (OrderProduct product: order.getProductsInOrder())
        {
            totalAmountOfProducts += product.getAmount();
        }
        return totalAmountOfProducts;
    }

//    public float getTotalCostOfAllProductsInOrder(Order order)
//    {
//
//    }
//
//    public float getTotalCostOfOrder(Order order)
//    {
//
//    }

    public Collection<Store> getAllStores()
    {
        return systemData.getStores().values();
    }

    public Collection<Product> getAllProducts()
    {
        return systemData.getProducts().values();
    }
}

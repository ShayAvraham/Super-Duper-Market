import jaxb.generated.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SystemData
{
    private Map<Integer,Product> products;
    private Map<Integer,Store> stores;

    public SystemData(SuperDuperMarketDescriptor marketDescription)
    {
        products = new HashMap<>();
        CreateProductsFromSDMItems(marketDescription);

        stores = new HashMap<>();
        CreateStoresFromSDMStores(marketDescription);

    }

    private void CreateProductsFromSDMItems(SuperDuperMarketDescriptor marketDescription)
    {
        SDMItems generatedItems = marketDescription.getSDMItems();
        for(SDMItem item: generatedItems.getSDMItem())
        {
            products.putIfAbsent(item.getId(),new Product(item));
        }

    }

    private void CreateStoresFromSDMStores(SuperDuperMarketDescriptor marketDescription)
    {
        SDMStores generatedIStores = marketDescription.getSDMStores();
        for(SDMStore store: generatedIStores.getSDMStore())
        {

            stores.putIfAbsent(store.getId(),new Store(store,CreateProductsInStore(store.getSDMPrices())));
        }
    }

    private Map<Product,Integer> CreateProductsInStore(SDMPrices generatedPrices)
    {
        Map<Product,Integer> productsInStore = new HashMap<>();
        for (SDMSell itemInStore : generatedPrices.getSDMSell())
        {
            Product product = products.get(itemInStore.getItemId());
            productsInStore.putIfAbsent(product,itemInStore.getPrice());
        }
        return  productsInStore;
    }

    public Map<Integer,Product> getProducts()
    {
        return products;
    }

    public Map<Integer,Store> getStores()
    {
        return stores;
    }
}

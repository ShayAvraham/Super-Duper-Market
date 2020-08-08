import jaxb.generated.*;

import java.util.ArrayList;
import java.util.Collection;

public class SystemData
{
    private Collection<Product> products;
    private Collection<Store> stores;

    public SystemData(SuperDuperMarketDescriptor marketDescription)
    {
        products = new ArrayList<Product>();
        CreateProductsFromSDMItems(marketDescription);

        stores = new ArrayList<Store>();
        CreateStoresFromSDMStores(marketDescription);
    }

    private void CreateProductsFromSDMItems(SuperDuperMarketDescriptor marketDescription)
    {
        SDMItems generatedItems = marketDescription.getSDMItems();
        for(SDMItem item: generatedItems.getSDMItem())
        {
            products.add(new Product(item));
        }

    }

    private void CreateStoresFromSDMStores(SuperDuperMarketDescriptor marketDescription)
    {
        SDMStores generatedIStores = marketDescription.getSDMStores();
        for(SDMStore store: generatedIStores.getSDMStore())
        {
            stores.add(new Store(store));
        }
    }

    public Collection<Product> getProducts()
    {
        return products;
    }

    public Collection<Store> getStores()
    {
        return stores;
    }
}

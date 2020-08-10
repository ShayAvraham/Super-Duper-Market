import exceptions.DuplicateValuesException;
import exceptions.InstanceNotExistException;
import jaxb.generated.*;

import java.util.*;

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

        CheckIfAllProductsInStores();

    }

    private void CheckIfAllProductsInStores()
    {
        for(Store store: stores.values())
        {
            //add to class product a proprety of ifProductIsInAnyStore
        }
    }


    private void CreateProductsFromSDMItems(SuperDuperMarketDescriptor marketDescription)
    {
        SDMItems generatedItems = marketDescription.getSDMItems();
        for(SDMItem item: generatedItems.getSDMItem())
        {
           if(products.putIfAbsent(item.getId(),new Product(item)) != null)
           {
               throw new DuplicateValuesException("product", item.getId());
           }
        }

    }

    private void CreateStoresFromSDMStores(SuperDuperMarketDescriptor marketDescription)
    {
        SDMStores generatedIStores = marketDescription.getSDMStores();
        for(SDMStore store: generatedIStores.getSDMStore())
        {
            Collection<StoreProduct> productsInStore = CreateProductsInStore(store.getSDMPrices());
           if(stores.putIfAbsent(store.getId(),new Store(store,productsInStore)) != null)
           {
               throw new DuplicateValuesException("store", store.getId());
           }
        }
    }

    private Collection<StoreProduct> CreateProductsInStore(SDMPrices generatedPrices)
    {
        Collection<StoreProduct> productsInStore = new HashSet<>();
        for (SDMSell itemInStore : generatedPrices.getSDMSell())
        {
            Product product = products.get(itemInStore.getItemId());
            if(product == null)
            {
                throw new InstanceNotExistException("product",itemInStore.getItemId());
            }
            if(!productsInStore.add(new StoreProduct(product, itemInStore.getPrice())))
            {
                throw new DuplicateValuesException("product in the store", product.getId());
            }
        }
        return productsInStore;
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

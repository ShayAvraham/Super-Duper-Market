import exceptions.DuplicateValuesException;
import exceptions.InstanceNotExistException;
import jaxb.generated.*;

import java.util.*;

public class SystemData
{
    private final String NOT_ALL_PRODUCTS_IN_STORE = "Error: There is a product that is not sold in any store";
    private Map<Integer,Product> products;
    private Map<Integer,Store> stores;
    private Set<Order> orders;

    private Set<Product>productsOnSale;

    public SystemData(SuperDuperMarketDescriptor marketDescription)
    {
        orders = new HashSet<>();
        products = new HashMap<>();
        CreateProductsFromSDMItems(marketDescription);

        productsOnSale = new HashSet<>();
        stores = new HashMap<>();
        CreateStoresFromSDMStores(marketDescription);
        CheckIfAllProductsInStores();

    }

    private void CheckIfAllProductsInStores()
    {
        if(productsOnSale.size()!=products.size())
        {
            throw new RuntimeException(NOT_ALL_PRODUCTS_IN_STORE);
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
            productsOnSale.add(product);
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

    public Set<Order> getOrders() {
        return orders;
    }
}

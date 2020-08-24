import exceptions.DuplicateValuesException;
import jaxb.generated.*;

import javax.management.InstanceNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class SystemData
{
    private final String NOT_ALL_PRODUCTS_IN_STORE = "The products with this i.d are not sold in any store: %1$s.";
    private final String PRODUCT_NOT_EXIST_MESSAGE = "Unable to sold the product with this id: %1$s," +
            " this product not defined in the system.";

    private Map<Integer,Product> products;
    private Map<Integer,Store> stores;
    private Set<Order> orders;
    private Set<Product>productsOnSale;

    public SystemData(SuperDuperMarketDescriptor marketDescription) throws InstanceNotFoundException
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
            Collection<Integer> notSoldProducts = new ArrayList<>();
            for (Product product : products.values())
            {
                if (!productsOnSale.contains(product))
                {
                    notSoldProducts.add(product.getId());
                }
            }
            throw new RuntimeException(String.format(NOT_ALL_PRODUCTS_IN_STORE,notSoldProducts.toString()));
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

    private void CreateStoresFromSDMStores(SuperDuperMarketDescriptor marketDescription) throws InstanceNotFoundException
    {
        SDMStores generatedIStores = marketDescription.getSDMStores();
        for(SDMStore store: generatedIStores.getSDMStore())
        {
            Collection<StoreProduct> productsInStore = CreateProductsInStore(store.getSDMPrices(),store.getId());
           if(stores.putIfAbsent(store.getId(),new Store(store,productsInStore)) != null)
           {
               throw new DuplicateValuesException("store", store.getId());
           }
        }
    }

    private Collection<StoreProduct> CreateProductsInStore(SDMPrices generatedPrices,int storeId) throws InstanceNotFoundException
    {
        Collection<StoreProduct> productsInStore = new HashSet<>();
        for (SDMSell itemInStore : generatedPrices.getSDMSell())
        {
            Product product = products.get(itemInStore.getItemId());
            if(product == null)
            {
                throw new InstanceNotFoundException(String.format(PRODUCT_NOT_EXIST_MESSAGE,itemInStore.getItemId()));
            }
            productsOnSale.add(product);
            if(!productsInStore.add(new StoreProduct(product, itemInStore.getPrice())))
            {
                String messageToException = String.format("product in store %1$s",storeId);
                throw new DuplicateValuesException(messageToException, product.getId());
            }

        }
        return productsInStore;
    }

    public void addNewOrder(Order newOrder, Map <Integer,Order> newSubOrders)
    {
        orders.add(newOrder);
        for(Integer storeId : newSubOrders.keySet())
        {
            stores.get(storeId).addNewOrder(newSubOrders.get(storeId));
        }
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



    public void removeProductFromStore(int storeId,int productId)
    {
        stores.get(storeId).removeProduct(productId);
    }

    public void addProductToStore(int storeId,int productId,int price)
    {
        StoreProduct newStoreProduct = new StoreProduct (products.get(productId),price);
        stores.get(storeId).addProduct(newStoreProduct);
    }

    public void updateProductPriceInStore(int storeId, int productId, int newPrice)
    {
        stores.get(storeId).updateProductPrice(productId, newPrice);
    }


}

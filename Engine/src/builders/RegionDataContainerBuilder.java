package builders;

import dataContainers.*;
import engineLogic.*;

import java.text.DecimalFormat;
import java.util.*;

public final class RegionDataContainerBuilder
{

    private static Map<Integer,ProductDataContainer> productsData;
    private static Map<Integer, StoreDataContainer> storesData;

    private static DecimalFormat DECIMAL_FORMAT;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    private RegionDataContainerBuilder()
    {
    }

    public static RegionDataContainer createRegionData(String ownerName,Region region)
    {
        productsData = createProductsData(region);
        storesData = createStoresData(region);

        return new RegionDataContainer(region.getName(),
                ownerName,
                region.getStores().size(),
                region.getNumOfOrders(),
                region.getOrderCostAvg(),
                productsData,
                storesData);
    }

    /** Create Products Data Containers **/
    private static Map<Integer,ProductDataContainer> createProductsData(Region region)
    {
        Map<Integer,ProductDataContainer> productsData = new HashMap<>();
        for (Product product:region.getProducts().values())
        {
            productsData.put(product.getId(),createProductData(product,region));
        }
        return productsData;
    }

    private static ProductDataContainer createProductData(Product product,Region region)
    {
        return new ProductDataContainer(
                product.getId(),
                product.getName(),
                product.getPurchaseForm().name(),
                region.getHowManyStoresSellProduct(product),
                Float.valueOf(DECIMAL_FORMAT.format(region.getProductAvgPrice(product))),
                region.getHowManyTimesProductSold(product));
    }

    /** Create Stores Data Containers **/
    private static Map<Integer, StoreDataContainer> createStoresData(Region region)
    {
        Map<Integer, StoreDataContainer> storesData = new HashMap<>();
        for (Store store:region.getStores().values())
        {
            storesData.put(store.getId(),createStoreData(store,region));
        }
        return storesData;
    }

    private static StoreDataContainer createStoreData(Store store, Region region)
    {
        return new StoreDataContainer(
                store.getId(),
                store.getName(),
                store.getOwnerName(),
                store.getPosition(),
                store.getPPK(),
                store.getStoreTotalIncomeFromDeliveries(),
                store.getStoreTotalIncomeFromProducts(),
                getStoreProductsData(store,region),
                getStoreOrdersData(store),
                getStoreDiscountsData(store));
    }

    private static Collection<ProductDataContainer> getStoreProductsData(Store store,Region region)
    {
        Collection<ProductDataContainer> allProductsData = new ArrayList<>();
        for (StoreProduct product: store.getStoreProducts())
        {
            allProductsData.add(new ProductDataContainer(
                    product.getId(),
                    product.getName(),
                    product.getPurchaseForm().name(),
                    getProductPricePerStore(product,region),
                    getSoldAmountPerStore(product,region)));
        }

        return allProductsData;
    }

    private static Map<Integer, Integer> getProductPricePerStore(StoreProduct selectedProduct, Region region)
    {
        Map<Integer, Integer> productPricePerStore = new HashMap<>();
        for (Store store: region.getStores().values())
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

    private static Map<Integer, Float> getSoldAmountPerStore(StoreProduct selectedProduct, Region region)
    {
        Map<Integer,Float> soldAmountPerStore = new HashMap<>();
        float soldAmount;
        for(Store store: region.getStores().values())
        {
            soldAmount = store.getHowManyTimesProductSold(selectedProduct);
            soldAmountPerStore.put(store.getId(),soldAmount);
        }
        return soldAmountPerStore;
    }

    private static Collection<OrderDataContainer> getStoreOrdersData(Store store)
    {
        Collection<OrderDataContainer> allOrdersData = new ArrayList<>();
        for (Order order: store.getStoreOrders())
        {
            allOrdersData.add(OrderDataContainerBuilder.createOrderData(order,storesData,productsData));
        }

        return allOrdersData;
    }

    private static Collection<DiscountDataContainer> getStoreDiscountsData(Store store)
    {
        Collection<DiscountDataContainer> allDiscountData = new ArrayList<>();
        if(!store.getStoreDiscounts().isEmpty())
        {
            for (Discount discount : store.getStoreDiscounts())
            {
                allDiscountData.add(DiscountDataContainerBuilder.createDiscountData(productsData,discount));
            }
        }
        return allDiscountData;
    }
}

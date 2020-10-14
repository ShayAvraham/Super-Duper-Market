package builders;

import dataContainers.*;
import engineLogic.*;

import java.text.DecimalFormat;
import java.util.*;

public class RegionDataContainerBuilder
{

    private Map<Integer,ProductDataContainer> productsData;
    private Map<Integer,StoreDataContainer> storesData;
    private Map<Integer,OrderDataContainer> ordersData;
    private RegionDataContainer regionData;

    private HashSet orders;

    private static DecimalFormat DECIMAL_FORMAT;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public RegionDataContainerBuilder(String ownerName,Region region)
    {
        productsData = createProductsData(region);
        storesData = createStoresData(region);
        ordersData = new HashMap<>();
        regionData = createRegionData(ownerName, region);
    }

    public  Map<Integer,ProductDataContainer> getAllProductsData() { return productsData; }

    public Map<Integer,StoreDataContainer> getStoresData() { return storesData; }

    public Map<Integer,OrderDataContainer> getOrdersData() { return ordersData; }

    public RegionDataContainer getRegionData() { return regionData; }

    public void updateData(Region region)
    {
        productsData = createProductsData(region);
        storesData = createStoresData(region);
        orders = new HashSet<>();
    }

    /** Create Products Data Containers **/
    private  Map<Integer,ProductDataContainer> createProductsData(Region region)
    {
        Map<Integer,ProductDataContainer> productsData = new HashMap<>();
        for (Product product:region.getProducts().values())
        {
            productsData.put(product.getId(),createProductData(product,region));
        }
        return productsData;
    }

    private ProductDataContainer createProductData(Product product,Region region)
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
    private Map<Integer,StoreDataContainer> createStoresData(Region region)
    {
        Map<Integer,StoreDataContainer> storesData = new HashMap<>();
        for (Store store:region.getStores().values())
        {
            storesData.put(store.getId(),createStoreData(store,region));
        }
        return storesData;
    }

    private StoreDataContainer createStoreData(Store store, Region region)
    {
        return new StoreDataContainer(
                store.getId(),
                store.getName(),
                store.getPosition(),
                store.getPPK(),
                store.getStoreTotalIncomeFromDeliveries(),
                getStoreProductsData(store,region),
                getStoreOrdersData(store),
                getStoreDiscountsData(store));
    }

    private Collection<ProductDataContainer> getStoreProductsData(Store store,Region region)
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

    private Map<Integer, Integer> getProductPricePerStore(StoreProduct selectedProduct,Region region)
    {
        Map<Integer,Integer> productPricePerStore = new HashMap<>();
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

    private Map<Integer, Float> getSoldAmountPerStore(StoreProduct selectedProduct, Region region)
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

    private Collection<OrderDataContainer> getStoreOrdersData(Store store)
    {
        Collection<OrderDataContainer> allOrdersData = new ArrayList<>();
        OrderDataContainerBuilder orderBuilder = new OrderDataContainerBuilder(productsData,storesData);
        UserDataContainerBuilder userBuilder = new UserDataContainerBuilder();
        for (Order order: store.getStoreOrders())
        {
            allOrdersData.add(orderBuilder.createOrderData(order));
        }

        return allOrdersData;
    }

    private Collection<DiscountDataContainer> getStoreDiscountsData(Store store)
    {
        Collection<DiscountDataContainer> allDiscountData = new ArrayList<>();
        if(!store.getStoreDiscounts().isEmpty())
        {
            for (Discount discount : store.getStoreDiscounts())
            {
                allDiscountData.add(createDiscountData(discount));
            }
        }

        return allDiscountData;
    }

    private DiscountDataContainer createDiscountData(Discount discount)
    {
        return new DiscountDataContainer(discount.getName(),
                discount.getDiscountType().name(),
                productsData.get(discount.getDiscountProduct().getId()),
                discount.getDiscountProduct().getAmountForDiscount(),
                createPriceForOfferProduct(discount.getProductsToOffer()),
                createAmountForOfferProduct(discount.getProductsToOffer()));
    }

    private Map<ProductDataContainer,Integer> createPriceForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Integer> priceForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            priceForOfferProduct.put(productsData.get(offerProduct.getId()),offerProduct.getOfferPrice());
        }
        return priceForOfferProduct;
    }

    private Map<ProductDataContainer,Double> createAmountForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Double> amountForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            amountForOfferProduct.put(productsData.get(offerProduct.getId()),offerProduct.getOfferAmount());
        }
        return amountForOfferProduct;
    }


    /** Create Region Data Container **/
    private RegionDataContainer createRegionData(String ownerName, Region region)
    {
        return new RegionDataContainer(region.getName(),
                ownerName,
                storesData.size(),
                region.getNumOfOrders(),
                region.getOrderCost());
    }





}

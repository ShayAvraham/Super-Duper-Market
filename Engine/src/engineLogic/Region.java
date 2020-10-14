package engineLogic;

import exceptions.DuplicateValuesException;
import exceptions.StoreDoesNotSellProductException;
import jaxb.generated.*;

import javax.management.InstanceNotFoundException;
import java.awt.*;
import java.util.*;

public class Region
{
    private final int MIN_BOUND = 1;
    private final int MAX_BOUND = 50;
    private final String POSITION_VALUES_OUT_OF_BOUNDS_MSG = "The store with i.d %1$s position is out of the bounds of [%2$s,%3$s]";
    private final String POSITION_ALREADY_TAKEN_MSG = "The position (%1$s,%2$s) already taken by another store";
    private final String NOT_ALL_PRODUCTS_IN_STORE = "The products with this i.d are not sold in any store: %1$s.";
    private final String PRODUCT_NOT_EXIST_MESSAGE = "Unable to sold the product with this id: %1$s," +
            " this product not defined in the system.";

    private String name;
    private Map<Integer,Product> products;
    private Map<Integer,Store> stores;
    private int numOfOrders;
    private float orderCost;

    public Region(SuperDuperMarketDescriptor marketDescription) throws InstanceNotFoundException
    {
        this.name = marketDescription.getSDMZone().getName();
        createProductsFromSDMItems(marketDescription.getSDMItems());
        createStoresFromSDMStores(marketDescription.getSDMStores());
        numOfOrders = 0;
        orderCost = 0;
    }

    public String getName()
    {
        return name;
    }

    public int getNumOfOrders()
    {
        return numOfOrders;
    }

    public float getOrderCost()
    {
        return orderCost;
    }

    private void createProductsFromSDMItems(SDMItems generatedItems)
    {
        products = new HashMap<>();
        for(SDMItem item: generatedItems.getSDMItem())
        {
            if(products.putIfAbsent(item.getId(),new Product(item)) != null)
            {
                throw new DuplicateValuesException("product", item.getId());
            }
        }
    }

    private void createStoresFromSDMStores(SDMStores generatedStores) throws InstanceNotFoundException
    {
        stores = new HashMap<>();
        HashSet<Point> storePositions = new HashSet<>();
        HashSet<Product> productsOnSale = new HashSet<>();

        for(SDMStore store: generatedStores.getSDMStore())
        {
            Point position = createPositionFromLocation(store,storePositions);
            Map<Integer,StoreProduct> storeProducts = createStoreProducts(store.getSDMPrices(),store.getId(),productsOnSale);
            Collection<Discount> storeDiscounts = createStoreDiscounts(store.getSDMDiscounts(),storeProducts,store.getId());

            if(stores.putIfAbsent(store.getId(), new Store(store, position, storeProducts.values(),storeDiscounts)) != null)
            {
                throw new DuplicateValuesException("store", store.getId());
            }
        }
        checkIfAllProductsInStores(productsOnSale);
    }

    private Point createPositionFromLocation(SDMStore store, HashSet<Point> storePositions)
    {
        Point position = new Point(store.getLocation().getX(),store.getLocation().getY());
        if((position.getX() > MAX_BOUND)||(position.getX() < MIN_BOUND)||(position.getY() > MAX_BOUND)||(position.getY() < MIN_BOUND))
        {
            throw new IndexOutOfBoundsException(String.format(POSITION_VALUES_OUT_OF_BOUNDS_MSG,store.getId(),MIN_BOUND,MAX_BOUND));
        }
        if(!storePositions.add(position))
        {
            throw new DuplicateValuesException(String.format(POSITION_ALREADY_TAKEN_MSG, position.getX(), position.getY()));
        }
        return position;
    }

    private Map<Integer,StoreProduct> createStoreProducts(SDMPrices generatedPrices,int storeId,HashSet<Product> productsOnSale) throws InstanceNotFoundException
    {

        Map<Integer,StoreProduct> storeProducts = new HashMap<>();
        for (SDMSell itemInStore : generatedPrices.getSDMSell())
        {
            validateProductInSystem(itemInStore.getItemId());
            Product product = products.get(itemInStore.getItemId());
            productsOnSale.add(product);
            if(storeProducts.putIfAbsent(product.getId(),new StoreProduct(product, itemInStore.getPrice())) != null)
            {
                String messageToException = String.format("product in store %1$s",storeId);
                throw new DuplicateValuesException(messageToException, product.getId());
            }

        }
        return storeProducts;
    }

    private void validateProductInSystem(int productId) throws InstanceNotFoundException
    {
        if(products.get(productId) == null)
        {
            throw new InstanceNotFoundException(String.format(PRODUCT_NOT_EXIST_MESSAGE,productId));
        }
    }

    private Collection<Discount> createStoreDiscounts(SDMDiscounts generatedDiscounts,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        Collection<Discount> storeDiscounts = new HashSet<>();
        if(generatedDiscounts != null)
        {
            for (SDMDiscount discount : generatedDiscounts.getSDMDiscount())
            {
                DiscountProduct discountProduct = createDiscountProduct(discount.getIfYouBuy(), storeProducts, storeId);
                Collection<OfferProduct> offerProducts = createOfferProducts(discount.getThenYouGet(), storeProducts, storeId);
                storeDiscounts.add(new Discount(discount.getName(),
                        discount.getThenYouGet().getOperator(),
                        discountProduct,
                        offerProducts));
            }
        }
        return storeDiscounts;
    }

    private DiscountProduct createDiscountProduct(IfYouBuy generatedIfYouBuy,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        validateProductSoldInStore(generatedIfYouBuy.getItemId(),storeProducts,storeId);
        StoreProduct storeProduct = storeProducts.get(generatedIfYouBuy.getItemId());
        return new DiscountProduct(storeProduct ,generatedIfYouBuy.getQuantity());
    }

    private Collection<OfferProduct> createOfferProducts(ThenYouGet generatedThenYouGet,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        Collection <OfferProduct> OfferProducts = new ArrayList<>();
        for (SDMOffer offer : generatedThenYouGet.getSDMOffer())
        {
            validateProductSoldInStore(offer.getItemId(),storeProducts,storeId);
            StoreProduct storeProduct = storeProducts.get(offer.getItemId());
            OfferProducts.add(new OfferProduct(storeProduct,offer.getForAdditional(),offer.getQuantity()));
        }
        return OfferProducts;
    }

    private void validateProductSoldInStore(int productId,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        validateProductInSystem(productId);
        if(storeProducts.get(productId) == null)
        {
            throw new StoreDoesNotSellProductException(storeId,productId);
        }
    }

    private void checkIfAllProductsInStores(HashSet<Product> productsOnSale)
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

    public int getHowManyStoresSellProduct(Product product)
    {
        int howManyStoresSellProduct = 0;

        for (Store store: stores.values())
        {
            if (store.isProductInStore(product))
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

        for (Store store: stores.values())
        {
            StoreProduct product = store.getProductById(selectedProduct.getId());
            if (product != null)
            {
                sumOfProductPrices += product.getPrice();
            }
        }
        return (sumOfProductPrices/numOfStoresWhoSellsProduct);
    }

    public float getHowManyTimesProductSold(Product product)
    {
        float howManyTimesProductSold = 0;

        for (Store store: stores.values())
        {
            howManyTimesProductSold += store.getHowManyTimesProductSold(product);
        }
        return howManyTimesProductSold;
    }



    /*******************************************************************************************************/

    public void addNewOrder(Map <Integer,Order> newSubOrders)
    {
        numOfOrders++;
        for(Integer storeId : newSubOrders.keySet())
        {
            stores.get(storeId).addNewOrder(newSubOrders.get(storeId));
            orderCost += newSubOrders.get(storeId).getCostOfAllProducts();
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

    public void removeProductFromStore(int storeId, int productId)
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

    public void addNewStore(Store newStore)
    {
        stores.put(newStore.getId(), newStore);
    }
}
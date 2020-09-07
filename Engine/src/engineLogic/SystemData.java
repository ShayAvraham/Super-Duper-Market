package engineLogic;

import exceptions.DuplicateValuesException;
import exceptions.StoreDoesNotSellProductException;
import jaxb.generated.*;

import javax.management.InstanceNotFoundException;
import javax.swing.text.Position;
import java.awt.*;
import java.util.*;

public class SystemData
{
    private final int MIN_BOUND = 1;
    private final int MAX_BOUND = 50;
    private final String POSITION_VALUES_OUT_OF_BOUNDS_MSG = "The %1$s with i.d %2$s position is out of the bounds of [%3$s,%4$s]";
//    private final String POSITION_ALREADY_TAKEN_MSG = "The position %1$s already taken by %2$s with i.d %3$s";
    private final String POSITION_ALREADY_TAKEN_MSG = "The position %1$s already taken by another entities";
    private final String NOT_ALL_PRODUCTS_IN_STORE = "The products with this i.d are not sold in any store: %1$s.";
    private final String PRODUCT_NOT_EXIST_MESSAGE = "Unable to sold the product with this id: %1$s," +
                                                     " this product not defined in the system.";

    private Map<Integer,Product> products;
    private Map<Integer,Store> stores;
    private Map<Integer,Customer> customers;
    private Set<Point> objectPositions;
    private Set<Order> orders;
    private Set<Product>productsOnSale;

    public SystemData(SuperDuperMarketDescriptor marketDescription) throws InstanceNotFoundException
    {
        objectPositions = new HashSet<>();
        products = new HashMap<>();
        productsOnSale = new HashSet<>();
        stores = new HashMap<>();
        orders = new HashSet<>();

        createCustomersFromSDMCustomers(marketDescription.getSDMCustomers());
        createProductsFromSDMItems(marketDescription.getSDMItems());
        createStoresFromSDMStores(marketDescription.getSDMStores());
        checkIfAllProductsInStores();
    }

    private void createCustomersFromSDMCustomers(SDMCustomers generatedCustomers)
    {
        for(SDMCustomer customer: generatedCustomers.getSDMCustomer())
        {
            Point position = createPositionFromLocation(customer.getLocation(),"customer",customer.getId());
            if(customers.putIfAbsent(customer.getId(),new Customer(customer,position)) != null)
            {
                throw new DuplicateValuesException("customer", customer.getId());
            }
        }
    }

    private Point createPositionFromLocation(Location location,String objectType,int id)
    {
        Point position = new Point(location.getX(),location.getY());
        if((position.getX() > MAX_BOUND)||(position.getX() < MIN_BOUND)||(position.getY() > MAX_BOUND)||(position.getY() < MIN_BOUND))
        {
            throw new IndexOutOfBoundsException(String.format(POSITION_VALUES_OUT_OF_BOUNDS_MSG,objectType,id,MIN_BOUND,MAX_BOUND));
        }
        if(!objectPositions.add(position))
        {
            throw new DuplicateValuesException(String.format(POSITION_ALREADY_TAKEN_MSG,position));
        }
        return position;
    }

    private void createProductsFromSDMItems(SDMItems generatedItems)
    {
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
        for(SDMStore store: generatedStores.getSDMStore())
        {
            Point position = createPositionFromLocation(store.getLocation(),"store",store.getId());
            Map<Integer,StoreProduct> storeProducts = createStoreProducts(store.getSDMPrices(),store.getId());
            Collection<Discount> storeDiscounts = createStoreDiscounts(store.getSDMDiscounts(),storeProducts,store.getId());

            if(stores.putIfAbsent(store.getId(), new Store(store, position, storeProducts.values(),storeDiscounts)) != null)
            {
                throw new DuplicateValuesException("store", store.getId());
            }
        }
    }

    private Map<Integer,StoreProduct> createStoreProducts(SDMPrices generatedPrices,int storeId) throws InstanceNotFoundException
    {
        Map<Integer,StoreProduct> storeProducts = new HashMap<>();
        for (SDMSell itemInStore : generatedPrices.getSDMSell())
        {
            Product product = products.get(itemInStore.getItemId());
            validateProductInSystem(product);
            productsOnSale.add(product);
            if(storeProducts.putIfAbsent(product.getId(),new StoreProduct(product, itemInStore.getPrice())) != null)
            {
                String messageToException = String.format("product in store %1$s",storeId);
                throw new DuplicateValuesException(messageToException, product.getId());
            }

        }
        return storeProducts;
    }

    private void validateProductInSystem(Product product) throws InstanceNotFoundException
    {
        if(product == null)
        {
            throw new InstanceNotFoundException(String.format(PRODUCT_NOT_EXIST_MESSAGE,product.getId()));
        }
    }

    private Collection<Discount> createStoreDiscounts(SDMDiscounts generatedDiscounts,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        Collection<Discount> storeDiscounts = new HashSet<>();
        for (SDMDiscount discount : generatedDiscounts.getSDMDiscount())
        {
            DiscountProduct discountProduct = createDiscountProduct(discount.getIfYouBuy(),storeProducts,storeId);
            Collection<OfferProduct> offerProducts = createOfferProducts(discount.getThenYouGet(),storeProducts,storeId);
            storeDiscounts.add(new Discount(discount.getName(),
                                            discount.getThenYouGet().getOperator(),
                                            discountProduct,
                                            offerProducts));
        }
        return storeDiscounts;
    }

    private DiscountProduct createDiscountProduct(IfYouBuy generatedIfYouBuy,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        StoreProduct storeProduct = storeProducts.get(generatedIfYouBuy.getItemId());
        validateProductSoldInStore(storeProduct,storeProducts,storeId);
        return new DiscountProduct(storeProduct ,generatedIfYouBuy.getQuantity());
    }

    private Collection<OfferProduct> createOfferProducts(ThenYouGet generatedThenYouGet,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        Collection <OfferProduct> OfferProducts = new ArrayList<>();
        for (SDMOffer offer : generatedThenYouGet.getSDMOffer())
        {
            StoreProduct storeProduct = storeProducts.get(offer.getItemId());
            validateProductSoldInStore(storeProduct,storeProducts,storeId);
            OfferProducts.add(new OfferProduct(storeProduct,offer.getForAdditional(),offer.getQuantity()));
        }
        return OfferProducts;
    }

    private void validateProductSoldInStore(StoreProduct storeProduct,Map<Integer,StoreProduct> storeProducts,int storeId) throws InstanceNotFoundException
    {
        validateProductInSystem(storeProduct);
        if(storeProduct == null)
        {
            throw new StoreDoesNotSellProductException(storeId,storeProduct.getId());
        }
    }

    private void checkIfAllProductsInStores()
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

    public Map<Integer, Customer> getCustomers()
    {
        return customers;
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
}

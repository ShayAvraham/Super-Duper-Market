package engineLogic;

import exceptions.DiscountRemoveException;
import jaxb.generated.SDMStore;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Store
{
    private static DecimalFormat DECIMAL_FORMAT;

    private int id;
    private String name;
    private Collection<StoreProduct> storeProducts;
    private Collection<Order> storeOrders;
    private Collection<Discount> storeDiscounts;
    private float ppk;
    private Point position;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    public Store(SDMStore store, Point position, Collection<StoreProduct> storeProducts, Collection<Discount>storeDiscounts)
    {
        this.id = store.getId();
        this.name = store.getName();
        this.position = position;
        this.ppk = store.getDeliveryPpk();
        this.storeProducts = new HashSet<>(storeProducts);
        this.storeDiscounts = storeDiscounts;
        this.storeOrders = new HashSet<>();
    }

    public Store(int id, String name, Point position, float ppk ,Collection<StoreProduct> storeProducts)
    {
        this.id = id;
        this.name = name;
        this.position = position;
        this.ppk = ppk;
        this.storeProducts = new HashSet<>(storeProducts);
        this.storeDiscounts = new HashSet<>();
        this.storeOrders = new HashSet<>();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPPK() {
        return ppk;
    }

    public Point getPosition() {
        return position;
    }

    public Collection<Order> getStoreOrders() {
        return storeOrders;
    }

    public Collection<StoreProduct> getStoreProducts() {
        return storeProducts;
    }

    public Collection<Discount> getStoreDiscounts() {
        return storeDiscounts;
    }

    public StoreProduct getProductById(int productId)
    {
        StoreProduct storeProduct = null;
        for (StoreProduct currentProduct: storeProducts)
        {
            if(currentProduct.getId() == productId)
            {
                storeProduct = currentProduct;
                break;
            }
        }
        return storeProduct;
    }

    public Discount getDiscountByName(String discountName)
    {
        Discount discount = null;
        for (Discount currentDiscount: storeDiscounts)
        {
            if(currentDiscount.getName() == discountName)
            {
                discount = currentDiscount;
                break;
            }
        }
        return discount;
    }

    public float getHowManyTimesProductSold(Product product)
    {
        float howManyTimesProductSold = 0;

        for (Order order: storeOrders)
        {
            howManyTimesProductSold += order.getProductAmountInOrder(product);
        }

        return howManyTimesProductSold;
    }

    public boolean isProductInStore(Product selectedProduct)
    {
        boolean isProductInStore = false;
        for (StoreProduct product: storeProducts)
        {
            if (product.getId() == selectedProduct.getId())
            {
                isProductInStore = true;
                break;
            }
        }
        return isProductInStore;
    }

    public void addNewOrder(Order newOrder)
    {
        storeOrders.add(newOrder);
    }

    public void removeProduct(int productId)
    {
        Product productToRemove = getProductById(productId);
        storeProducts.remove(productToRemove);
        removeDiscounts(productToRemove);
    }

    private void removeDiscounts(Product product)
    {
        Collection <String> removeDiscounts = new ArrayList<>();
        for (Discount discount:storeDiscounts)
        {
            if(discount.getDiscountProduct().getId() == product.getId())
            {
                storeDiscounts.remove(discount);
                removeDiscounts.add(discount.getName());
            }
        }
        if(!removeDiscounts.isEmpty())
        {
            throw new DiscountRemoveException(product.getName(),removeDiscounts);
        }
    }

    public void addProduct(StoreProduct productToAdd)
    {
        storeProducts.add(productToAdd);
    }

    public void updateProductPrice(int productId, int newPrice)
    {
        getProductById(productId).setPrice(newPrice);
    }

    public float getStoreTotalIncomeFromDeliveries()
    {
        float totalIncomeFromDeliveries = 0;

        for (Order order: storeOrders)
        {
            totalIncomeFromDeliveries += order.getDeliveryCost();
        }
        return totalIncomeFromDeliveries;
    }

    @Override
    public String toString() {
        return "engineLogic.Store details: " + "\n" +
                "ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Price per kilometer: " + ppk + "\n";
    }
}
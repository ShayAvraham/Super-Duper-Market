package engineLogic;

import java.awt.*;

public class StoreNotice implements Notice
{
    private String storeOwner;
    private String storeName;
    private Point location;
    private int amountOfProductsForSale;
    private int amountOfRegionProducts;

    public StoreNotice(String storeOwner, String storeName, Point location, int amountOfProductsForSale, int amountOfRegionProducts) {
        this.storeOwner = storeOwner;
        this.storeName = storeName;
        this.location = location;
        this.amountOfProductsForSale = amountOfProductsForSale;
        this.amountOfRegionProducts = amountOfRegionProducts;
    }

    public String getStoreOwner() {
        return storeOwner;
    }

    public String getStoreName() {
        return storeName;
    }

    public Point getLocation() {
        return location;
    }

    public int getAmountOfProductsForSale() {
        return amountOfProductsForSale;
    }

    public int getAmountOfRegionProducts() {
        return amountOfRegionProducts;
    }
}

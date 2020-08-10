
public class OrderProduct
{
    private StoreProduct storeProduct;
    private int amount;

    public OrderProduct(StoreProduct storeProduct, int amount) {
        this.storeProduct = storeProduct;
        this.amount = amount;
    }

    public StoreProduct getStoreProduct() {
        return storeProduct;
    }

    public void setStoreProduct(StoreProduct storeProduct) {
        this.storeProduct = storeProduct;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

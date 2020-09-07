package engineLogic;

public class OrderProduct extends StoreProduct
{
    private float amount;

    public OrderProduct(StoreProduct storeProduct, float amount)
    {
        super(storeProduct);
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }
}

import java.util.Objects;

public class OrderProduct extends StoreProduct
{
    private float amount;

    public OrderProduct(StoreProduct storeProduct, int amount)
    {
        super(storeProduct);
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

//    public void setAmount(int amount) {
//        this.amount = amount;
//    }

}

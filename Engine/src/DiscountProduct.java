public class DiscountProduct extends StoreProduct
{
    private float amountForDiscount;

    public DiscountProduct(StoreProduct storeProduct, float amountForDiscount)
    {
        super(storeProduct);
        this.amountForDiscount = amountForDiscount;
    }

    public float getAmountForDiscount()
    {
        return amountForDiscount;
    }
}

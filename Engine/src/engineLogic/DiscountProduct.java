package engineLogic;

public class DiscountProduct extends StoreProduct
{
    private double amountForDiscount;

    public DiscountProduct(StoreProduct storeProduct, double amountForDiscount)
    {
        super(storeProduct);
        this.amountForDiscount = amountForDiscount;
    }

    public double getAmountForDiscount()
    {
        return amountForDiscount;
    }
}

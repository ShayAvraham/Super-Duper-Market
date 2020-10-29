package engineLogic;

public class OfferProduct extends StoreProduct
{
    private int offerPrice;
    private double offerAmount;

    public OfferProduct(StoreProduct storeProduct,int offerPrice,double offerAmount)
    {
        super(storeProduct);
        this.offerPrice = offerPrice;
        this.offerAmount = offerAmount;
    }

    public int getOfferPrice()
    {
        return offerPrice;
    }

    public double getOfferAmount()
    {
        return offerAmount;
    }
}

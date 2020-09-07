public class OfferProduct extends StoreProduct
{
    private int offerPrice;
    private float offerAmount;

    public OfferProduct(StoreProduct storeProduct,int offerPrice,float offerAmount)
    {
        super(storeProduct);
        this.offerPrice = offerPrice;
        this.offerAmount = offerAmount;
    }

    public int getOfferPrice()
    {
        return offerPrice;
    }

    public float getOfferAmount()
    {
        return offerAmount;
    }


}

import java.util.Objects;

public class StoreProduct extends Product
{
    private float price;

    public StoreProduct(Product product, int price)
    {
        super(product);
        this.price = price;
    }

    protected StoreProduct(StoreProduct storeProduct)
    {
        super(storeProduct);
        this.price = storeProduct.price;
    }


    public float getPrice() {
        return price;
    }

//    public void setProduct(Product product) {
//        this.product = product;
//    }

//    public void setPrice(int price) { this.price = price; }

}

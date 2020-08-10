public class StoreProduct
{
    private Product product;
    private int price;

    public StoreProduct(Product product, int price)
    {
        this.product = product;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

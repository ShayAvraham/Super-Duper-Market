import java.util.Objects;

public class StoreProduct
{
    private Product product;
    private float price;

    public StoreProduct(Product product, int price)
    {
        this.product = product;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public float getPrice() {
        return price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(int price) { this.price = price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreProduct)) return false;
        StoreProduct that = (StoreProduct) o;
        return getProduct().equals(that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct());
    }
}

import java.util.Collection;
import java.util.Objects;

public class Discount
{
    public enum DiscountType
    {
        ONE_OF, ALL_OR_NOTHING
    }

    private String name;
    private DiscountProduct product;
    private Collection <OfferProduct> productsToOffer;
    private DiscountType type;

    public Discount(String name, DiscountProduct product, Collection<OfferProduct> productsToOffer, DiscountType type)
    {
        this.name = name;
        this.product = product;
        this.productsToOffer = productsToOffer;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public DiscountProduct getProduct()
    {
        return product;
    }

    public Collection<OfferProduct> getProductsToOffer()
    {
        return productsToOffer;
    }

    public DiscountType getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Discount)) return false;
        Discount discount = (Discount) o;
        return getName().equals(discount.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }
}

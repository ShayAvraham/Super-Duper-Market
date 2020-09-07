package engineLogic;

import java.util.Collection;
import java.util.Objects;

public class Discount
{
    public enum DiscountType
    {
        ONE_OF, ALL_OR_NOTHING, IRRELEVANT
    }

    private String name;
    private DiscountProduct product;
    private Collection <OfferProduct> productsToOffer;
    private DiscountType discountType;

    public Discount(String name,String discountType,DiscountProduct product, Collection<OfferProduct> productsToOffer)
    {
        this.name = name;
        this.product = product;
        this.productsToOffer = productsToOffer;
        createDiscountType(discountType);
    }

    private void createDiscountType(String discountType)
    {
        switch (discountType)
        {
            case "ONE-OF":
                this.discountType = DiscountType.ONE_OF;
                break;
            case "ALL-OR-NOTHING":
                this.discountType = DiscountType.ALL_OR_NOTHING;
                break;
            default:
                this.discountType = DiscountType.IRRELEVANT;

        }
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

    public DiscountType getDiscountType() {
        return discountType;
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

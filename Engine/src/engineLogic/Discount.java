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
    private DiscountProduct discountProduct;
    private Collection <OfferProduct> productsToOffer;
    private DiscountType discountType;

    public Discount(String name,String discountType,DiscountProduct discountProduct, Collection<OfferProduct> productsToOffer)
    {
        this.name = name;
        this.discountProduct = discountProduct;
        this.productsToOffer = productsToOffer;
        createDiscountType(discountType);
    }

    private void createDiscountType(String discountType)
    {
        switch (discountType)
        {
            case "ONE-OF":
            case "ONE_OF":
                this.discountType = DiscountType.ONE_OF;
                break;
            case "ALL_OR_NOTHING":
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

    public DiscountProduct getDiscountProduct()
    {
        return discountProduct;
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

    public float getOfferProductAmount(Product product)
    {
        float amount = 0;
        for (OfferProduct offerProduct: productsToOffer)
        {
            if(offerProduct.getId() == product.getId())
            {
                amount = new Double(offerProduct.getOfferAmount()).floatValue();
                break;
            }
        }
        return amount;
    }
}

package dataContainers;

import javafx.beans.property.*;
import java.util.Map;
import java.util.Objects;

public class DiscountDataContainer
{
    private String discountName;
    private String ifYouBuyDescription;
    private String thenYouGetDescription;

    private String discountType;
    private ProductDataContainer discountProduct;
    private double amountForDiscount;

    private Map<Integer,Integer> priceForOfferProduct;
    private Map<Integer,Double> amountForOfferProduct;
    private int selectedOfferID = 0;


    public DiscountDataContainer(String discountName, String discountType, ProductDataContainer discountProduct,
                                 double amountForDiscount, Map<Integer,Integer> priceForOfferProduct,
                                 Map<Integer,Double> amountForOfferProduct,String ifYouBuyDescription, String thenYouGetDescription)
    {
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountProduct = discountProduct;
        this.amountForDiscount = amountForDiscount;
        this.priceForOfferProduct = priceForOfferProduct;
        this.amountForOfferProduct = amountForOfferProduct;
        this.ifYouBuyDescription = ifYouBuyDescription;
        this.thenYouGetDescription = thenYouGetDescription;
    }

    public String getDiscountName() {
        return discountName;
    }

    public String getIfYouBuyDescription() {
        return ifYouBuyDescription;
    }

    public String getThenYouGetDescription() {
        return thenYouGetDescription;
    }

    public String getDiscountType() {
        return discountType;
    }

    public ProductDataContainer getDiscountProduct() {
        return discountProduct;
    }

    public double getAmountForDiscount() {
        return amountForDiscount;
    }

    public Map<Integer, Integer> getPriceForOfferProduct() {
        return priceForOfferProduct;
    }

    public Map<Integer, Double> getAmountForOfferProduct() {
        return amountForOfferProduct;
    }

    public int getSelectedOfferID() {
        return selectedOfferID;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof DiscountDataContainer)) return false;
        DiscountDataContainer that = (DiscountDataContainer) o;
        return getDiscountName().equals(that.getDiscountName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDiscountName());
    }
}

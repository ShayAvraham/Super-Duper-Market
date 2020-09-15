package dataContainers;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.Collection;
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
    private Map<ProductDataContainer,Integer> priceForOfferProduct;
    private Map<ProductDataContainer,Double> amountForOfferProduct;


    private final BooleanProperty checked = new SimpleBooleanProperty(false);
    private SimpleObjectProperty<ProductDataContainer> selectedOfferProduct = new SimpleObjectProperty<>();

    public DiscountDataContainer(String discountName, String discountType, ProductDataContainer discountProduct,
                                 double amountForDiscount, Map<ProductDataContainer,Integer> priceForOfferProduct,
                                 Map<ProductDataContainer,Double> amountForOfferProduct)
    {
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountProduct = discountProduct;
        this.amountForDiscount = amountForDiscount;
        this.priceForOfferProduct = priceForOfferProduct;
        this.amountForOfferProduct = amountForOfferProduct;
//        selectedOfferProduct.setValue(discountType == "ONE_OF"? null: priceForOfferProduct.keySet().stream().findFirst().get());
        createIfYouBuyDescription();
        createThenYouGetDescription();
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

    public Map<ProductDataContainer, Integer> getPriceForOfferProduct() {
        return priceForOfferProduct;
    }

    public Map<ProductDataContainer, Double> getAmountForOfferProduct() {
        return amountForOfferProduct;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public ProductDataContainer getSelectedOfferProduct() {
        return selectedOfferProduct.get();
    }

    public SimpleObjectProperty<ProductDataContainer> selectedOfferProductProperty() {
        return selectedOfferProduct;
    }

    private void createIfYouBuyDescription()
    {
        String purchaseFormStr = createPurchaseFormStr(discountProduct);
        ifYouBuyDescription = String.format("%1$s %2$s of %3$s",amountForDiscount,purchaseFormStr,discountProduct.getName());
    }

    private String createPurchaseFormStr(ProductDataContainer product)
    {
        String purchaseFormStr = "";
        switch (product.getPurchaseForm())
        {
            case "WEIGHT":
                purchaseFormStr = "kg";
                break;
            case "QUANTITY":
                purchaseFormStr = "unit";
                if(amountForDiscount > 1)
                {
                    purchaseFormStr += "s";
                }
                break;
        }
        return purchaseFormStr;
    }

    private void createThenYouGetDescription()
    {
        thenYouGetDescription = "";
        String discountTypeStr = createDiscountTypeStr();

        for(ProductDataContainer offerProduct : priceForOfferProduct.keySet())
        {
            String purchaseFormStr = createPurchaseFormStr(offerProduct);
            if(!offerProduct.equals(priceForOfferProduct.keySet().stream().findFirst().get()))
            {
                thenYouGetDescription += discountTypeStr;
            }
            thenYouGetDescription += String.format(" %1$s %2$s of %3$s for %4$s",
                    amountForOfferProduct.get(offerProduct),
                    purchaseFormStr,
                    offerProduct.getName(),
                    priceForOfferProduct.get(offerProduct));
        }
    }

    private String createDiscountTypeStr()
    {
        String discountTypeStr = "";
        switch (discountType)
        {
            case "ONE_OF":
                discountTypeStr = " or";
                break;
            case "ALL_OR_NOTHING":
                discountTypeStr = " and";
                break;
        }
        return discountTypeStr;
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

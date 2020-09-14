package dataContainers;

import java.util.Collection;
import java.util.Map;

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


    public DiscountDataContainer(String discountName, String discountType, ProductDataContainer discountProduct,
                                 double amountForDiscount,Map<ProductDataContainer,Integer> priceForOfferProduct,
                                 Map<ProductDataContainer,Double> amountForOfferProduct)
    {
        this.discountName = discountName;
        this.discountType = discountType;
        this.discountProduct = discountProduct;
        this.amountForDiscount = amountForDiscount;
        this.priceForOfferProduct = priceForOfferProduct;
        this.amountForOfferProduct = amountForOfferProduct;
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
}

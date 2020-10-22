package builders;

import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
import engineLogic.Discount;
import engineLogic.DiscountProduct;
import engineLogic.OfferProduct;
import engineLogic.Product;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class DiscountDataContainerBuilder
{
    private static Map<Integer,ProductDataContainer> productsData;

    private DiscountDataContainerBuilder()
    {
    }

    public static DiscountDataContainer createDiscountData(Map<Integer,ProductDataContainer> products,Discount discount)
    {
        productsData = products;
        return new DiscountDataContainer(discount.getName(),
                discount.getDiscountType().name(),
                productsData.get(discount.getDiscountProduct().getId()),
                discount.getDiscountProduct().getAmountForDiscount(),
                createPriceForOfferProduct(discount.getProductsToOffer()),
                createAmountForOfferProduct(discount.getProductsToOffer()),
                createIfYouBuyDescription(discount.getDiscountProduct()),
                createThenYouGetDescription(discount));
    }

    private static Map<Integer,Integer> createPriceForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <Integer,Integer> priceForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            priceForOfferProduct.put(offerProduct.getId(),offerProduct.getOfferPrice());
        }
        return priceForOfferProduct;
    }

    private static Map<Integer,Double> createAmountForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <Integer,Double> amountForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            amountForOfferProduct.put(offerProduct.getId(),offerProduct.getOfferAmount());
        }
        return amountForOfferProduct;
    }

    private static String createIfYouBuyDescription(DiscountProduct discountProduct)
    {
        String purchaseFormStr = createPurchaseFormStr(discountProduct,discountProduct.getAmountForDiscount() > 1? true:false);
        return String.format("%1$s %2$s of %3$s",discountProduct.getAmountForDiscount(),purchaseFormStr,discountProduct.getName());
    }

    private static String createPurchaseFormStr(Product product,boolean isBiggerThanOne)
    {
        String purchaseFormStr = "";
        switch (product.getPurchaseForm().name())
        {
            case "WEIGHT":
                purchaseFormStr = "kg";
                break;
            case "QUANTITY":
                purchaseFormStr = "unit";
                if(isBiggerThanOne)
                {
                    purchaseFormStr += "s";
                }
                break;
        }
        return purchaseFormStr;
    }

    private static String createThenYouGetDescription(Discount discount)
    {
        String thenYouGetDescription = "";
        String discountTypeStr = createDiscountTypeStr(discount.getDiscountType());

        for(OfferProduct offerProduct : discount.getProductsToOffer())
        {
            String purchaseFormStr = createPurchaseFormStr(offerProduct,offerProduct.getOfferAmount()>1?true:false);
            if(!offerProduct.equals(discount.getProductsToOffer().stream().findFirst().get()))
            {
                thenYouGetDescription += discountTypeStr;
            }
            thenYouGetDescription += String.format(" %1$s %2$s of %3$s for %4$s",
                    offerProduct.getOfferAmount(),
                    purchaseFormStr,
                    offerProduct.getName(),
                    offerProduct.getOfferPrice());
        }
        return thenYouGetDescription;
    }

    private static String createDiscountTypeStr(Discount.DiscountType discountType)
    {
        String discountTypeStr = "";
        switch (discountType.name())
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

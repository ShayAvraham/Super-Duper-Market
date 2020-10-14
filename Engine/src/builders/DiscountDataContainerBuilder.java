package builders;

import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
import engineLogic.Discount;
import engineLogic.OfferProduct;

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
                createAmountForOfferProduct(discount.getProductsToOffer()));
    }

    private static Map<ProductDataContainer,Integer> createPriceForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Integer> priceForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            priceForOfferProduct.put(productsData.get(offerProduct.getId()),offerProduct.getOfferPrice());
        }
        return priceForOfferProduct;
    }

    private static Map<ProductDataContainer,Double> createAmountForOfferProduct(Collection<OfferProduct> productsToOffer)
    {
        Map <ProductDataContainer,Double> amountForOfferProduct = new HashMap<>();
        for (OfferProduct offerProduct:productsToOffer)
        {
            amountForOfferProduct.put(productsData.get(offerProduct.getId()),offerProduct.getOfferAmount());
        }
        return amountForOfferProduct;
    }
}

package builders;

import dataContainers.*;
import engineLogic.Discount;
import engineLogic.Order;
import engineLogic.OrderProduct;
import engineLogic.Store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class OrderDataContainerBuilder
{
    private static Map<Integer,ProductDataContainer> productsData;
    private static Map<Integer,StoreDataContainer> storesData;

    private OrderDataContainerBuilder()
    {

    }

    public static OrderDataContainer createOrderData(Order order,Map<Integer,StoreDataContainer> stores,
                                              Map<Integer,ProductDataContainer> products)
    {
        storesData = stores;
        productsData = products;
        return new OrderDataContainer(
                order.getId(),
                order.getDate(),
                UserDataContainerBuilder.createUserData(order.getCustomer()),
                createOrderProductsData(order),
                createOrderDiscountsData(order),
                order.isDynamic(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCost(),
                order.getProductTypesAmountInOrder());
    }



    private static Map<StoreDataContainer, Collection<ProductDataContainer>> createOrderProductsData(Order order)
    {
        Map<StoreDataContainer, Collection<ProductDataContainer>> orderProducts = new HashMap<>();
        for(Store store : order.getProducts().keySet())
        {
            Collection<ProductDataContainer> products = new ArrayList<>();
            for(OrderProduct product : order.getProducts().get(store))
            {
                products.add(new ProductDataContainer(productsData.get(product.getId()),product.getAmount()));
            }
            orderProducts.put(storesData.get(store.getId()),products);
        }
        return  orderProducts;
    }

    private static Map<StoreDataContainer, Collection<DiscountDataContainer>> createOrderDiscountsData(Order order)
    {
        Map<StoreDataContainer, Collection<DiscountDataContainer>> orderDiscounts = new HashMap<>();
        if(!order.getDiscounts().isEmpty())
        {
            for (Store store : order.getDiscounts().keySet())
            {
                Collection<DiscountDataContainer> discountsData = new ArrayList<>();
                for (Discount discount : order.getDiscounts().get(store))
                {
                    discountsData.add(DiscountDataContainerBuilder.createDiscountData(productsData,discount));
                }
                orderDiscounts.put(storesData.get(store.getId()), discountsData);
            }
        }
        return orderDiscounts;
    }
}

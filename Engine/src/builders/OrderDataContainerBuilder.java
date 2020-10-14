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

public class OrderDataContainerBuilder
{
    private Map<Integer,ProductDataContainer> productsData;
    private Map<Integer,StoreDataContainer> storesData;

    public OrderDataContainerBuilder(Map<Integer,ProductDataContainer> productsData, Map<Integer,StoreDataContainer> storesData)
    {
        this.productsData = productsData;
        this.storesData = storesData;
    }

    public OrderDataContainer createOrderData(Order order)
    {
        UserDataContainerBuilder userBuilder = new UserDataContainerBuilder();
        return new OrderDataContainer(
                order.getId(),
                order.getDate(),
                userBuilder.createUserData(order.getCustomer()),
                createOrderProductsData(order),
                createOrderDiscountsData(order),
                order.isDynamic(),
                order.getCostOfAllProducts(),
                order.getDeliveryCost(),
                order.getTotalCost(),
                order.getProductTypesAmountInOrder());
    }



    private Map<StoreDataContainer, Collection<ProductDataContainer>> createOrderProductsData(Order order)
    {
        Map<StoreDataContainer, Collection<ProductDataContainer>> orderProducts = new HashMap<>();
        for(Store store : order.getProducts().keySet())
        {
            Collection<ProductDataContainer> productsData = new ArrayList<>();
            for(OrderProduct product : order.getProducts().get(store))
            {
                productsData.add(new ProductDataContainer(this.productsData.get(product.getId()),product.getAmount()));
            }
            orderProducts.put(storesData.get(store.getId()),productsData);
        }
        return  orderProducts;
    }

    private Map<StoreDataContainer, Collection<DiscountDataContainer>> createOrderDiscountsData(Order order)
    {
        Map<StoreDataContainer, Collection<DiscountDataContainer>> orderDiscounts = new HashMap<>();
        if(!order.getDiscounts().isEmpty())
        {
            for (Store store : order.getDiscounts().keySet())
            {
                Collection<DiscountDataContainer> discountsData = new ArrayList<>();
                DiscountDataContainerBuilder discountBuilder = new DiscountDataContainerBuilder(productsData);
                for (Discount discount : order.getDiscounts().get(store))
                {
                    discountsData.add(discountBuilder.createDiscountData(discount));
                }
                orderDiscounts.put(storesData.get(store.getId()), discountsData);
            }
        }
        return orderDiscounts;
    }
}

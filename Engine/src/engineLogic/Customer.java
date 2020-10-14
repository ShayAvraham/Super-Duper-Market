package engineLogic;

import java.util.HashSet;
import java.util.Set;

public class Customer extends User
{
    private Set<Order> orders;

    public Customer (int id, String name)
    {
        super(id,name);
        orders = new HashSet<>();
    }

    public double getCustomerOrderCostAvg()
    {
        return orders.stream()
                .mapToDouble(Order::getCostOfAllProducts)
                .average()
                .orElse(0.0);
    }

    public double getCustomerDeliveryCostAvg()
    {
        return orders.stream()
                .mapToDouble(Order::getDeliveryCost)
                .average()
                .orElse(0.0);
    }

    public Set<Order> getOrders()
    {
        return orders;
    }

    public void addOrder(Order order)
    {
        orders.add(order);
    }
}

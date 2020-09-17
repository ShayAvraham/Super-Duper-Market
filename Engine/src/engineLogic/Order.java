package engineLogic;
import dataContainers.*;

import java.time.LocalDate;
import java.util.*;

public class Order
{
    private static int idNumber = 1;

    private int id;
    private Customer customer;
    private LocalDate date;
    private Map<Store, Collection<OrderProduct>> products;
    private Map<Store, Collection<Discount>> discounts;
    private boolean isDynamic;
    private float costOfAllProducts;
    private float deliveryCost;
    private float totalCost;


    /** Create new Order**/
    public Order(LocalDate date, Customer customer, Map<Store, Collection<OrderProduct>> products,
                 Map<Store, Collection<Discount>> discounts, boolean isDynamic, float costOfAllProducts,
                 float deliveryCost, float totalCost)
    {
        this.id = idNumber++;
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
    }

    /** Create Sub Order**/
    public Order(int id,LocalDate date, Customer customer, Map<Store, Collection<OrderProduct>> products,
                 Map<Store, Collection<Discount>> discounts,boolean isDynamic, float costOfAllProducts,
                 float deliveryCost, float totalCost)
    {
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.discounts = discounts;
        this.isDynamic = isDynamic;
        this.costOfAllProducts = costOfAllProducts;
        this.deliveryCost = deliveryCost;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<Store, Collection<OrderProduct>> getProducts() {
        return products;
    }

    public Map<Store, Collection<Discount>> getDiscounts() {
        return discounts;
    }

    public boolean isDynamic() {
        return isDynamic;
    }

    public float getCostOfAllProducts() {
        return costOfAllProducts;
    }

    public float getDeliveryCost() {
        return deliveryCost;
    }

    public float getTotalCost() {
        return totalCost;
    }

//
//    public float getCostOfAllProducts()
//    {
//        float costOfAllProducts = 0;
//        for (OrderProduct orderProduct: orderedProducts)
//        {
//            costOfAllProducts += orderProduct.getAmount() * orderProduct.getPrice();
//        }
//        return  costOfAllProducts;
//    }



    public float getProductAmountInOrder(Product product)
    {
        float productAmountInOrder = 0;

        for(Store store : products.keySet())
        {
            for (OrderProduct orderProduct : products.get(store))
            {
                if (orderProduct.getId() == product.getId())
                {
                    productAmountInOrder = orderProduct.getAmount();
                    break;
                }
            }
        }
        return productAmountInOrder;
    }

//    public void addOrderProduct(OrderProduct productToAdd)
//    {
//        orderedProducts.add(productToAdd);
//    }
//
//    public int getAllOrderedProductsQuantity()
//    {
//        int allOrderedProductsQuantity = 0;
//
//        for (OrderProduct orderProduct: orderedProducts)
//        {
//            float productQuantity = orderProduct.getAmount();
//            if(orderProduct.getPurchaseForm() == Product.ProductPurchaseForm.WEIGHT)
//            {
//                productQuantity = 1;
//            }
//            allOrderedProductsQuantity += productQuantity;
//
//        }
//        return allOrderedProductsQuantity;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

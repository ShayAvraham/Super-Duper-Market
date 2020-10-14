package engineLogic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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

    public float getProductAmountInOrder(Product product)
    {
        float productAmountInOrder = getProductAmount(product);
        productAmountInOrder += getProductAmountInOrderDiscounts(product);
        return productAmountInOrder;
    }

    private float getProductAmount(Product product)
    {
        float amount = 0;
        for(Store store : products.keySet())
        {
            for (OrderProduct orderProduct : products.get(store))
            {
                if (orderProduct.getId() == product.getId())
                {
                    amount = orderProduct.getAmount();
                    break;
                }
            }
        }
        return  amount;
    }

    private float getProductAmountInOrderDiscounts(Product product)
    {
        float amount = 0;
        for(Store store : discounts.keySet())
        {
            for (Discount discount : discounts.get(store))
            {
                amount += discount.getOfferProductAmount(product);
            }
        }

        return amount;
    }

    public int getProductTypesAmountInOrder()
    {
        int numOfProductsTypes = getProductTypesAmount();
        numOfProductsTypes += getProductTypesAmountInOrderDiscounts();
        return numOfProductsTypes;
    }

    private int getProductTypesAmount()
    {
        int amount = 0;
        for(Store store : products.keySet())
        {
            amount += products.get(store).size();
        }
        return  amount;
    }

    private int getProductTypesAmountInOrderDiscounts()
    {
        int amount = 0;
        ArrayList productsId = getProductsIdList();
        for(Store store : discounts.keySet())
        {
            for (Discount discount : discounts.get(store))
            {
                for (OfferProduct offerProduct : discount.getProductsToOffer())
                {
                    if(!productsId.contains(offerProduct.getId()))
                    {
                        productsId.add(offerProduct.getId());
                        amount++;
                    }
                }
            }
        }

        return amount;
    }

    private ArrayList<Integer> getProductsIdList()
    {
        ArrayList<Integer> productsId = new ArrayList<>();
        for(Store store : products.keySet())
        {
            for (OrderProduct orderProduct : products.get(store))
            {
                productsId.add(orderProduct.getId());
            }
        }
        return productsId;
    }


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

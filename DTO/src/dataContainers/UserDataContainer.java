package dataContainers;

import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDataContainer
{
    private int id;
    private String name;
    private String role;
    private int numOfOrders;
    private float orderCostAvg;
    private float deliveryCostAvg;
    private Collection<TransactionDataContainer> transactions;
    private float balance;


    /** new user form client to server **/
    public UserDataContainer(String name, String role)
    {
        this.id = 0;
        this.name = name;
        this.role = role;
        this.numOfOrders = 0;
        this.orderCostAvg = 0;
        this.deliveryCostAvg = 0;
        this.transactions = new ArrayList<>();
        this.balance = 0;
    }

    /** from exist user **/
    public UserDataContainer(int id, String name,String role,int numOfOrders, float orderCostAvg,
                             float deliveryCostAvg,float balance,Collection<TransactionDataContainer> transactions)
    {
        this.id = id;
        this.name = name;
        this.role = role;
        this.numOfOrders = numOfOrders;
        this.orderCostAvg = orderCostAvg;
        this.deliveryCostAvg = deliveryCostAvg;
        this.transactions = transactions;
        this.balance = balance;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public float getOrderCostAvg() {
        return orderCostAvg;
    }

    public float getDeliveryCostAvg() {
        return deliveryCostAvg;
    }

    public String getRole() {
        return role;
    }

    public Collection<TransactionDataContainer> getTransactions() {
        return transactions;
    }

    public float getBalance() {
        return balance;
    }

    public void addTransactionDataContainer(TransactionDataContainer transactionDataContainer)
    {
        transactions.add(transactionDataContainer);
    }

    @Override
    public String toString()
    {
        return name +" | " +
                "id:" + id;
    }
}

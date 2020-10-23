package dataContainers;

import java.util.ArrayList;
import java.util.Collection;

public class UserDataContainer
{
    private int id;
    private String name;
    private String role;
    private Collection<TransactionDataContainer> transactions;
    private Collection<NoticeDataContainer> notices;
    private float balance;


    /** new user form client to server **/
    public UserDataContainer(String name, String role)
    {
        this.id = 0;
        this.name = name;
        this.role = role;
        this.transactions = new ArrayList<>();
        this.notices = new ArrayList<>();
        this.balance = 0;
    }

    /** from exist user **/
    public UserDataContainer(int id, String name,String role,float balance,
                             Collection<TransactionDataContainer> transactions,
                             Collection<NoticeDataContainer> notices)
    {
        this.id = id;
        this.name = name;
        this.role = role;
        this.transactions = transactions;
        this.notices = notices;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Collection<NoticeDataContainer> getNotices() {
        return notices;
    }

    @Override
    public String toString()
    {
        return name +" | " +
                "id:" + id;
    }
}

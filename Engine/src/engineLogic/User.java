package engineLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class User
{
    private String name;
    private int id;
    private float balance;
    private Collection<Transaction> transactions;
    private static int idNumber = 1;

    protected User(String name,float balance, Collection<Transaction>transactions)
    {
        this.id = idNumber++;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions;
    }

    public String getName()
    {
        return name;
    }

    public Integer getId()
    {
        return id;
    }

    public float getBalance()
    {
        return balance;
    }

    public Collection<Transaction> getTransactions()
    {
        return transactions;
    }

    public void addTransaction(Transaction.TransactionCategory transactionCategory, Date date, float cost)
    {
        float balanceAfter = transactionCategory.equals(Transaction.TransactionCategory.TRANSFER)
                ? balance - cost : balance + cost;
        Transaction transaction = new Transaction(transactionCategory,
                date, cost, balance, balanceAfter);
        transactions.add(transaction);
        balance = balanceAfter;
    }
}

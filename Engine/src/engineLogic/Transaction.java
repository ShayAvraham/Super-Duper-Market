package engineLogic;

import java.time.LocalDate;
import java.util.Date;

public class Transaction
{
    public enum TransactionCategory
    {
        CHARGING, RECEIVE, TRANSFER
    }

    private TransactionCategory transactionCategory;
    private LocalDate date;
    private float cost;
    private float balanceBefore;
    private float balanceAfter;

    public Transaction(TransactionCategory transactionCategory, LocalDate date, float cost,
                       float balanceBefore, float balanceAfter)
    {
        this.transactionCategory = transactionCategory;
        this.date = date;
        this.cost = cost;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public TransactionCategory getTransactionCategory()
    {
        return transactionCategory;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getCost() {
        return cost;
    }

    public float getBalanceBefore() {
        return balanceBefore;
    }

    public float getBalanceAfter() {
        return balanceAfter;
    }
}

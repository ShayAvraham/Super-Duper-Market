package dataContainers;

import java.time.LocalDate;

public class TransactionDataContainer
{
    private String transactionCategory;
    private LocalDate date;
    private float cost;
    private float balanceBefore;
    private float balanceAfter;

    public TransactionDataContainer(String transactionCategory, LocalDate date,
                                    float cost, float balanceBefore, float balanceAfter)
    {
        this.transactionCategory = transactionCategory;
        this.date = date;
        this.cost = cost;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public String getTransactionCategory()
    {
        return transactionCategory;
    }

    public LocalDate getDate()
    {
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

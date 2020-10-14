package builders;

import dataContainers.TransactionDataContainer;
import dataContainers.UserDataContainer;
import engineLogic.Customer;
import engineLogic.Transaction;
import engineLogic.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public final class UserDataContainerBuilder
{
    private static DecimalFormat DECIMAL_FORMAT;

    static
    {
        DECIMAL_FORMAT = new DecimalFormat("#.##");
    }

    private UserDataContainerBuilder()
    {
    }

    public static UserDataContainer createUserData(User user)
    {
        return new UserDataContainer(
                user.getId(),
                user.getName(),
                user instanceof Customer ? "customer":"owner",
                user instanceof Customer ? ((Customer) user).getOrders().size():0,
                user instanceof Customer? Float.valueOf(DECIMAL_FORMAT.format(((Customer) user).getCustomerOrderCostAvg())):0,
                user instanceof Customer? Float.valueOf(DECIMAL_FORMAT.format(((Customer) user).getCustomerDeliveryCostAvg())):0,
                user.getBalance(),
                createUserTransactionsData(user.getTransactions()));
    }

    private static Collection<TransactionDataContainer> createUserTransactionsData(Collection<Transaction> transactions)
    {
        Collection<TransactionDataContainer> transactionsData = new ArrayList<>();
        for (Transaction transaction: transactions)
        {
            transactionsData.add(createTransactionsData(transaction));
        }
        return transactionsData;
    }

    private static TransactionDataContainer createTransactionsData(Transaction transaction)
    {
        return new TransactionDataContainer(transaction.getTransactionCategory().name().toLowerCase(),
                transaction.getDate(),
                transaction.getCost(),
                transaction.getBalanceBefore(),
                transaction.getBalanceAfter());
    }
}

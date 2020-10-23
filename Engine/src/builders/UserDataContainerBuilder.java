package builders;

import dataContainers.NoticeDataContainer;
import dataContainers.TransactionDataContainer;
import dataContainers.UserDataContainer;
import engineLogic.*;

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
                user.getBalance(),
                createUserTransactionsData(user.getTransactions()),
                createUserFeedbacksData(user));
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

    private static Collection<NoticeDataContainer> createUserFeedbacksData(User user)
    {
        Collection<NoticeDataContainer> feedbackData = new ArrayList<>();
        if(user instanceof Owner)
        {
//            for (Feedback feedback: ((Owner) user).getNotices())
//            {
//                feedbackData.add(createFeedbacksData(feedback));
//            }
        }
        return feedbackData;
    }

    private static NoticeDataContainer createFeedbacksData(Feedback feedback)
    {
//        return new NoticeDataContainer(feedback.getRegionName(),
//                feedback.getStoreID(),
//                feedback.getCustomerName(),
//                feedback.getRank(),
//                feedback.getDescription(),
//                feedback.getDate());
        return null;
    }
}

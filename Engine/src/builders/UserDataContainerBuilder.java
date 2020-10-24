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
                createUserNoticesData(user));
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

    private static Collection<NoticeDataContainer> createUserNoticesData(User user)
    {
        Collection<NoticeDataContainer> noticeData = new ArrayList<>();
        if(user instanceof Owner)
        {
            for (Notice notice: ((Owner) user).getNotices())
            {
                noticeData.add(createNoticeData(notice));
            }
        }
        return noticeData;
    }

    private static NoticeDataContainer createNoticeData(Notice notice)
    {
        if(notice instanceof Feedback)
        {
            return creatNoticeDataFromFeedback((Feedback) notice);
        }
        else
        {
           return creatNoticeDataFromFeedback((OrderNotice) notice);
        }
    }

    private static NoticeDataContainer creatNoticeDataFromFeedback(Feedback feedback)
    {
        String description = "Feedback \n" +
                             "--------\n" +
                            "Customer Name: " +feedback.getCustomerName() + "\n" +
                            "Rank: " + feedback.getRank() + "\n";
        return new NoticeDataContainer(description);
    }

    private static NoticeDataContainer creatNoticeDataFromFeedback(OrderNotice orderNotice)
    {
        String description = "Order \n" +
                             "----------\n" +
                    "Order I.D: " + orderNotice.getOrderId() +"\n" +
                    "Customer name: " + orderNotice.getCustomerName() + "\n" +
                    "Number of products types: " + orderNotice.getNumOfProductsType() + "\n" +
                    "Products cost: " + orderNotice.getProductsCost() + "\n" +
                    "Delivery cost: " + orderNotice.getDeliveryCost() + "\n";
        return new NoticeDataContainer(description);
    }
}

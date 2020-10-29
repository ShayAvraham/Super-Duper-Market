package builders;

import dataContainers.NoticeDataContainer;
import dataContainers.TransactionDataContainer;
import dataContainers.UserDataContainer;
import engineLogic.*;

import java.awt.*;
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
        else if (notice instanceof OrderNotice)
        {
           return creatNoticeDataFromOrderNotice((OrderNotice) notice);
        }
        else
        {
            return creatNoticeDataFromStoreNotice((StoreNotice) notice);
        }
    }


    private static NoticeDataContainer creatNoticeDataFromFeedback(Feedback feedback)
    {
        String strMessage = "Customer " +feedback.getCustomerName() + "\n" +
                            "gave your store rank of " + feedback.getRank() +  "." + "\n";

        return new NoticeDataContainer(feedback.getRegionName(),
                feedback.getStoreID(),
                feedback.getCustomerName(),
                feedback.getRank(),
                feedback.getDescription(),
                feedback.getDate(),
                strMessage);
    }

    private static NoticeDataContainer creatNoticeDataFromOrderNotice(OrderNotice orderNotice)
    {
        String strMessage = "Customer " + orderNotice.getCustomerName() + " added a new order " + "(" +
                orderNotice.getOrderId() + ")\n" +
                             "-\n" +
                    "the order included " + orderNotice.getNumOfProductsType() + " types of products, \n" +
                    "products cost " + DECIMAL_FORMAT.format(orderNotice.getProductsCost()) + ", \n" +
                    "delivery cost " + DECIMAL_FORMAT.format(orderNotice.getDeliveryCost()) + ".\n";
        return new NoticeDataContainer(orderNotice.getOrderId(),
                orderNotice.getNumOfProductsType(),
                orderNotice.getProductsCost(),
                orderNotice.getDeliveryCost(),
                orderNotice.getCustomerName(),
                strMessage);
    }

    private static NoticeDataContainer creatNoticeDataFromStoreNotice(StoreNotice storeNotice)
    {
        Point location = storeNotice.getLocation();
        String strMessage = storeNotice.getStoreOwner() + " added a new store - \n" +
                            "Store name: " + storeNotice.getStoreName() + ", \n" +
                            "Location: " + "(" + location.getX() + "," + location.getY() + ")" + ", \n" +
                            "Products on sale: " + storeNotice.getAmountOfProductsForSale() + "/" +
                            storeNotice.getAmountOfProductsForSale() + ".\n";

        return new NoticeDataContainer(storeNotice.getStoreOwner(),
                storeNotice.getStoreName(),
                location,
                storeNotice.getAmountOfProductsForSale(),
                storeNotice.getAmountOfRegionProducts(),
                strMessage);
    }
}

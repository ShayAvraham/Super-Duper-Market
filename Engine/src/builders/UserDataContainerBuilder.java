package builders;

import dataContainers.UserDataContainer;
import engineLogic.Customer;
import engineLogic.User;

import java.text.DecimalFormat;

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
                user instanceof Customer? Float.valueOf(DECIMAL_FORMAT.format(((Customer) user).getCustomerDeliveryCostAvg())):0);
    }
}

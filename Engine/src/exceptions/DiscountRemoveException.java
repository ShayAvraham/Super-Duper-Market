package exceptions;

import java.util.Collection;

public class DiscountRemoveException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "As a result of removing product %1$s the following discounts has been remove: %2$s";
    private String message;


    public DiscountRemoveException(String productName,Collection<String> removedDiscounts)
    {
        String discountNames = "";
        for (String discount:removedDiscounts)
        {
                if(!removedDiscounts.stream().findFirst().equals(discount))
                {
                    discountNames += ", ";
                }
                discountNames += discount;
        }
        this.message = String.format(EXCEPTION_MESSAGE,productName,discountNames);
    }
    public String getMessage()
    {
        return message;
    }
}
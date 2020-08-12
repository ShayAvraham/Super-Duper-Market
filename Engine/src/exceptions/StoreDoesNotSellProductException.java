package exceptions;

import java.awt.*;

public class StoreDoesNotSellProductException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Sorry, this stor do not sell this product.";


    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE);
    }
}

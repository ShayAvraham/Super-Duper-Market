package exceptions;

import java.awt.*;

public class StoreDoesNotSellProductException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Sorry, this store do not sell this product.";//change


    public String getMessage()
    {
        return EXCEPTION_MESSAGE;
    }
}

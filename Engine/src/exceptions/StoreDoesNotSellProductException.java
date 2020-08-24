package exceptions;

public class StoreDoesNotSellProductException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Sorry, this store do not sell this product.";


    public String getMessage()
    {
        return EXCEPTION_MESSAGE;
    }
}

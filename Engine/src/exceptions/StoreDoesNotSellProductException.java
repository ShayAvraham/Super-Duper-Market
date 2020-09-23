package exceptions;

public class StoreDoesNotSellProductException extends RuntimeException
{
    private final String EXCEPTION_DETAILED_MESSAGE = "Store %1$s do not sell product %2$s.";
    private String message;

    public StoreDoesNotSellProductException(int storeId, int productId)
    {
        this.message = String.format(EXCEPTION_DETAILED_MESSAGE, storeId, productId);
    }

    public String getMessage()
    {
        return message;
    }
}

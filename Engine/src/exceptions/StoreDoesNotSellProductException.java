package exceptions;

public class StoreDoesNotSellProductException extends RuntimeException
{
    private final String DEFAULT_EXCEPTION_MESSAGE = "\nSorry, this store do not sell this product.";
    private final String EXCEPTION_DETAILED_MESSAGE = "Store %1$s do not sell product %2$s.";
    private String message;

    public StoreDoesNotSellProductException(int storeId, int productId)
    {
        this.message = String.format(EXCEPTION_DETAILED_MESSAGE, storeId, productId);
    }

    public StoreDoesNotSellProductException()
    {
        this.message = DEFAULT_EXCEPTION_MESSAGE;
    }


    public String getMessage()
    {
        return message;
    }
}

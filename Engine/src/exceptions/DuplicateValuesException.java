package exceptions;

public class DuplicateValuesException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "There is a %1$s already exists with this id: %2$s";
    private int id;
    private String instanceName;
    private String message;


    public DuplicateValuesException(String instanceName,int id)
    {
        this.id = id;
        this.instanceName = instanceName;
        this.message = String.format(EXCEPTION_MESSAGE, instanceName, id);
    }

    public DuplicateValuesException(String message)
    {
        this.message = message;
    }


    public String getMessage()
    {
        return message;
    }
}

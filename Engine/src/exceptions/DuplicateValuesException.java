package exceptions;

public class DuplicateValuesException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Cause of failure: There is a %1$s already exists with this id: %2$s";
    private int id;
    private String instanceName;


    public DuplicateValuesException(String instanceName,int id)
    {
        this.id = id;
        this.instanceName = instanceName;
    }


    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE, instanceName, id);
    }
}

package exceptions;

import java.awt.*;

public class InstanceNotExistException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "There is not a %1$s with this id: %2$s";//change
    private int id;
    private String instanceName;


    public InstanceNotExistException(String instanceName,int id)
    {
        this.id = id;
        this.instanceName = instanceName;
    }

    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE, instanceName, id);
    }
}

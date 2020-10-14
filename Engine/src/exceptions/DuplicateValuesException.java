package exceptions;

import java.awt.*;

public class DuplicateValuesException extends RuntimeException
{
    private final String ID_EXCEPTION_MESSAGE = "There is a %1$s already exists with this id: %2$s";
    private final String LOCATION_EXCEPTION_MESSAGE = "There is a %1$s already exists with this location: %2$s";
    private final String NAME_EXCEPTION_MESSAGE = "There is a %1$s already exists with this name: %2$s";

    private static final String POSITION_FORMAT = "(%1$s, %2$s)";
    private String message;


    public DuplicateValuesException(String instanceName,int id)
    {
        this.message = String.format(ID_EXCEPTION_MESSAGE, instanceName, id);
    }

    public DuplicateValuesException(String instanceName,String name)
    {
        this.message = String.format(NAME_EXCEPTION_MESSAGE, instanceName, name);
    }

    public DuplicateValuesException(String instanceName, Point pos)
    {
        String posStr = String.format(POSITION_FORMAT, (int)pos.getX(), (int)pos.getY());
        this.message = String.format(LOCATION_EXCEPTION_MESSAGE, instanceName, posStr);
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
package exceptions;

import java.awt.*;

public class UserLocationNotValidException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "\nSorry, the location [%1$s,%2$s] is not within the allowable range.";
    private Point userLocation;


    public UserLocationNotValidException(Point userLocation)
    {
        this.userLocation = userLocation;
    }


    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE, (int) userLocation.getX(), (int) userLocation.getY());
    }
}

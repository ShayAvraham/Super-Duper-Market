package exceptions;

import java.awt.*;

public class UserLocationNotValidException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Sorry, the location %1$s is not within the allowable range.";
    private Point userLocation;


    public UserLocationNotValidException(Point userLocation)
    {
        this.userLocation = userLocation;
    }


    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE, userLocation);
    }
}

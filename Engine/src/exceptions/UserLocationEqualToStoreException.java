package exceptions;

import java.awt.*;

public class UserLocationEqualToStoreException extends RuntimeException
{
    private final String EXCEPTION_MESSAGE = "Sorry, the location [%1$s,%2$s] is same as the store location.";
    private Point userLocation;
    private Point storeLocation;

    public UserLocationEqualToStoreException(Point userLocation, Point storeLocation)
    {
        this.userLocation = userLocation;
        this.storeLocation = storeLocation;
    }


    public String getMessage()
    {
        return String.format(EXCEPTION_MESSAGE, userLocation.getX(), userLocation.getY());
    }
}

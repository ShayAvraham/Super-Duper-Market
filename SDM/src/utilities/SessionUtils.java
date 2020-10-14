package utilities;

import dataContainers.UserDataContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils
{
    private static final String USER = "user";

    public static UserDataContainer getUser(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USER) : null;
        return sessionAttribute != null ? (UserDataContainer) sessionAttribute : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
package utilities;

import dataContainers.UserDataContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils
{
    private static final String USER = "user";
    private static final String REGION = "region";


    public static UserDataContainer getUser(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(USER) : null;
        return sessionAttribute != null ? (UserDataContainer) sessionAttribute : null;
    }

    public static String getRegionName(HttpServletRequest request)///change
    {
//        HttpSession session = request.getSession(false);
//        Object sessionAttribute = session != null ? session.getAttribute(REGION) : null;
      //  return sessionAttribute != null ? (String) sessionAttribute : null;

        return "Galil Maarvi";
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
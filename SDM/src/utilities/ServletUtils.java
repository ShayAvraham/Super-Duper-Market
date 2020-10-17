package utilities;

import managers.SystemManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "systemManager";
    private static final Object userManagerLock = new Object();


    public static SystemManager getSystemManager(ServletContext servletContext) {
        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new SystemManager());
            }
        }
        return (SystemManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            return Integer.parseInt(value);
        }
        throw new NullPointerException();
    }
}

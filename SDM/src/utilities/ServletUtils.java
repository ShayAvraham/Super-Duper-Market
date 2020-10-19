package utilities;

import com.google.gson.Gson;
import managers.SystemManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, Collection<Integer>> getIntToCollectionIntMapParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Map<String, Collection<String>> tempMap = new Gson().fromJson(value, HashMap.class);
        Map<Integer, Collection<Integer>> resultMap = new HashMap<>();
        for (String key : tempMap.keySet()) {
            Collection<Integer> values = new ArrayList<>();
            for (String stringValue : tempMap.get(key)) {
                values.add(Integer.parseInt(stringValue));
            }
            resultMap.put(Integer.parseInt(key), values);
        }
        return resultMap;
    }

    public static Map<Integer, Float> getIntToFloatMapParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Map<String, String> tempMap = new Gson().fromJson(value, HashMap.class);
        Map<Integer, Float> resultMap = new HashMap<>();
        for(String key : tempMap.keySet())
        {
            resultMap.put(Integer.parseInt(key),Float.parseFloat(tempMap.get(key)));
        }
        return resultMap;
    }

    public static Collection<Integer> getIntCollectionParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Collection<String> tempCollection = new Gson().fromJson(value, ArrayList.class);
        Collection<Integer> resultCollection = new ArrayList<>();
        for(String elem : tempCollection)
        {
            resultCollection.add(Integer.parseInt(elem));
        }
        return resultCollection;
    }
}

package utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
import managers.SystemManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
//import java.lang.reflect.Type;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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

    public static Collection<ProductDataContainer> getProductsCollectionParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Gson json = new Gson();
        Type listType = new TypeToken<ArrayList<ProductDataContainer>>() {}.getType();
        ArrayList<ProductDataContainer> products = new Gson().fromJson(value, listType);
        return products;
    }

    public static Map<Integer, Collection<ProductDataContainer>> getIntegerToCollectionProductMapParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Type listType = new TypeToken<Map<Integer, ArrayList<ProductDataContainer>>>() {}.getType();
        Map<Integer, Collection<ProductDataContainer>> IntegerToProducts = new Gson().fromJson(value, listType);
        return IntegerToProducts;
    }

    public static Map<Integer, Float> getIntToFloatMapParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Type mapType = new TypeToken<Map<Integer,Float>>() {}.getType();
        Map<Integer,Float> InToFloat = new Gson().fromJson(value, mapType);
        return InToFloat;
    }

    public static Map<Integer, Collection<DiscountDataContainer>> getIntegerToCollectionDiscountMapParameter
            (HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        Type mapType = new TypeToken<Map<Integer, ArrayList<DiscountDataContainer>>>() {}.getType();
        Map<Integer, Collection<DiscountDataContainer>> IntegerToDiscounts = new Gson().fromJson(value, mapType);
        return IntegerToDiscounts;
    }

    public static Date getDateParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date localDate = null;
        try
        {
            localDate = format.parse(value);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return localDate;
    }

    public static Float getFloatParameter(HttpServletRequest request, String parameterName)
    {
        String value = request.getParameter(parameterName);
        if (value != null) {
            return Float.parseFloat(value);
        }
        throw new NullPointerException();
    }
}

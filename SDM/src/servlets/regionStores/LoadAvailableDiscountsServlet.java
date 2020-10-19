package servlets.regionStores;

import com.google.gson.Gson;
import dataContainers.DiscountDataContainer;
import dataContainers.StoreDataContainer;
import managers.SystemManager;
import utilities.ServletUtils;
import utilities.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

public class LoadAvailableDiscountsServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Map<Integer,Collection<Integer>> selectedProducts = ServletUtils.getIntToCollectionIntMapParameter(request,"selectedProducts");
        Map<Integer,Float> productsAmounts = ServletUtils.getIntToFloatMapParameter(request,"productsAmounts");
        Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts = systemManager.getAvailableDiscounts(regionName,selectedProducts,productsAmounts);
        Gson json = new Gson();
        String jsonResponse = json.toJson(availableDiscounts);
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

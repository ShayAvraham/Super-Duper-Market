package servlets.regionStores;

import com.google.gson.Gson;
import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
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
import java.util.ArrayList;
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

        Map<Integer, Collection<ProductDataContainer>> storesToBuyFrom =
                ServletUtils.getIntegerToCollectionProductMapParameter(request,"storesToBuyFrom");
        Map<Integer,Float> productsAmounts = ServletUtils.getIntToFloatMapParameter(request,"productsAmounts");
        Map<StoreDataContainer,Collection<DiscountDataContainer>> availableDiscounts =
                systemManager.getAvailableDiscounts(regionName,storesToBuyFrom,productsAmounts);
        Gson json = new Gson();
        Collection<AvailableDiscounts> output = new ArrayList<>();
        for (StoreDataContainer store: availableDiscounts.keySet())
        {
            output.add(new AvailableDiscounts(store, availableDiscounts.get(store)));
        }
        String jsonResponse = json.toJson(output);
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    private static class AvailableDiscounts {

        final private Collection<DiscountDataContainer> discounts;
        final private StoreDataContainer store;


        public AvailableDiscounts(StoreDataContainer store, Collection<DiscountDataContainer> discounts) {
            this.discounts = discounts;
            this.store = store;
        }
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

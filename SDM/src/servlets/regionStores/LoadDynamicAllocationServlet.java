package servlets.regionStores;

import com.google.gson.Gson;
import dataContainers.DiscountDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import engineLogic.Product;
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
import java.util.List;
import java.util.Map;

public class LoadDynamicAllocationServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
            response.setContentType("application/json");
            String regionName = SessionUtils.getRegionName(request);
            SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
            Collection<Integer> selectedProducts = ServletUtils.getIntCollectionParameter(request, "selectedProducts");
            Map<StoreDataContainer, Collection<ProductDataContainer>> storesToBuyFrom =
                    systemManager.dynamicStoreAllocation(regionName,selectedProducts);
            Gson json = new Gson();

            Collection<StoreAndProducts> output = new ArrayList<>();
            for (StoreDataContainer store: storesToBuyFrom.keySet())
            {
                output.add(new StoreAndProducts(store, storesToBuyFrom.get(store)));
            }
//            String jsonResponse = json.toJson(storesToBuyFrom);
                    String jsonResponse = json.toJson(output);
        response.setStatus(200);
            PrintWriter out = response.getWriter();
            out.print(jsonResponse);
            out.flush();
    }

    private static class StoreAndProducts {

        final private Collection<ProductDataContainer> products;
        final private StoreDataContainer store;

        public StoreAndProducts(StoreDataContainer store,Collection<ProductDataContainer> products) {
            this.products = products;
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

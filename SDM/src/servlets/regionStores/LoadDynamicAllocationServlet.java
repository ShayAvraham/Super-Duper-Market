package servlets.regionStores;

import com.google.gson.Gson;
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

public class LoadDynamicAllocationServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Collection<ProductDataContainer> selectedProducts = ServletUtils.getProductsCollectionParameter(request, "selectedProducts");

        Map<StoreDataContainer, Collection<ProductDataContainer>> storesToBuyFrom =
                systemManager.dynamicStoreAllocation(regionName,selectedProducts);

        Gson json = new Gson();
        Collection<StoresToBuyFrom> output = new ArrayList<>();
        for (StoreDataContainer store: storesToBuyFrom.keySet())
        {
            output.add(new StoresToBuyFrom(store, storesToBuyFrom.get(store)));
        }
        String jsonResponse = json.toJson(output);
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    public class StoresToBuyFrom {

        private Collection<ProductDataContainer> products;
        private StoreDataContainer store;


        public StoresToBuyFrom(StoreDataContainer store, Collection<ProductDataContainer> products) {
            this.products = products;
            this.store = store;
        }

        public Collection<ProductDataContainer> getProducts() {
            return products;
        }

        public StoreDataContainer getStore() {
            return store;
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

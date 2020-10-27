package servlets.regionStores.addNewStore;

import com.google.gson.Gson;
import dataContainers.ProductDataContainer;
import dataContainers.UserDataContainer;
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

public class AddNewStoreServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        UserDataContainer user = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Integer storeID = ServletUtils.getIntParameter(request,"id");
        String  storeName = request.getParameter("name");
        Integer xPosition = ServletUtils.getIntParameter(request,"xPosition");
        Integer yPosition = ServletUtils.getIntParameter(request,"yPosition");
        Integer storePPK = ServletUtils.getIntParameter(request,"ppk");
        Collection<ProductDataContainer> selectedProducts =
                ServletUtils.getProductsCollectionParameter(request, "selectedProducts");
        systemManager.AddNewStore(regionName,user.getId(),storeID,storeName,xPosition,yPosition,storePPK,selectedProducts);
        Gson json = new Gson();
        String jsonResponse = json.toJson("yes");
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

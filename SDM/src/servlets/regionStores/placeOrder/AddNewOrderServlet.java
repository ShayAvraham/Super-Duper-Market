package servlets.regionStores.placeOrder;

import com.google.gson.Gson;
import dataContainers.DiscountDataContainer;
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
import java.util.Date;
import java.util.Map;

public class AddNewOrderServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        UserDataContainer user = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Map<Integer, Collection<ProductDataContainer>> storesToBuyFrom =
                ServletUtils.getIntegerToCollectionProductMapParameter(request,"storesToBuyFrom");
        Map<Integer, Collection<DiscountDataContainer>> selectedDiscounts =
                ServletUtils.getIntegerToCollectionDiscountMapParameter(request,"selectedDiscounts");
        Map<Integer,Float> productsAmounts = ServletUtils.getIntToFloatMapParameter(request,"productsAmounts");
        Date deliveryDate = ServletUtils.getDateParameter(request,"deliveryDate");
        Integer xPosition = ServletUtils.getIntParameter(request,"xDestinationPosition");
        Integer yPosition = ServletUtils.getIntParameter(request,"yDestinationPosition");
        String orderType = request.getParameter("orderType");
        Float productsCost = ServletUtils.getFloatParameter(request,"productsCost");
        Float deliveryCost = ServletUtils.getFloatParameter(request,"deliveryCost");
        Float totalOrderCost = ServletUtils.getFloatParameter(request,"totalOrderCost");

        systemManager.AddNewOrder(storesToBuyFrom,selectedDiscounts,productsAmounts,deliveryDate,xPosition,
                yPosition,orderType,productsCost,deliveryCost,totalOrderCost,regionName,user.getId());

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

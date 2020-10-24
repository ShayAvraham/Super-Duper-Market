package servlets.regionStores.placeOrder;

import com.google.gson.Gson;
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
import java.util.Date;

public class AddNewFeedbackServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        UserDataContainer user = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Integer storeID = ServletUtils.getIntParameter(request,"storeID");
        Date date = ServletUtils.getDateParameter(request,"date");
        Integer rank = ServletUtils.getIntParameter(request,"rank");
        String description = request.getParameter("description");
        systemManager.addNewFeedback(regionName,storeID,user.getName(),rank,description,date);
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

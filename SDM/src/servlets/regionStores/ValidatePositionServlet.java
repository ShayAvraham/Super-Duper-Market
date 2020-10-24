package servlets.regionStores;

import com.google.gson.Gson;
import managers.SystemManager;
import utilities.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ValidatePositionServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Integer xPosition = ServletUtils.getIntParameter(request,"xDestinationPosition");
        Integer yPosition = ServletUtils.getIntParameter(request,"yDestinationPosition");
        boolean isPositionValid = systemManager.ValidatePosition(xPosition,yPosition);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(isPositionValid);
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

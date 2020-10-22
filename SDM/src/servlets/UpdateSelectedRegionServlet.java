package servlets;

import com.google.gson.Gson;

import dataContainers.RegionDataContainer;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;


public class UpdateSelectedRegionServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        Gson json = new Gson();
        String jsonResponse;
        try
        {
            String selectedRegionName = request.getParameter("selectedRegionName");
            request.getSession(false).setAttribute("region", selectedRegionName);
            jsonResponse = json.toJson("yes");
            response.setStatus(200);
        }
        catch (Exception e)
        {
            response.setStatus(404);
            jsonResponse = json.toJson("No");
        }
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

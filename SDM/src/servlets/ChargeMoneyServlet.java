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

public class ChargeMoneyServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        UserDataContainer userFromSession = SessionUtils.getUser(request);
//        float amountToCharge = (Float) request.getAttribute("amount");
//        LocalDate datePicked = (LocalDate) request.getAttribute("date");
        float amountToCharge = ServletUtils.getFloatParameter(request, "amount");
        LocalDate datePicked = ServletUtils.getLocalDateParameter(request, "date");
        try
        {
            systemManager.ChargeMoneyInUserAccount(userFromSession.getId(), amountToCharge, datePicked);
            response.setStatus(200);
        }
        catch (Exception e)
        {
            response.setStatus(404);
            response.getWriter().print(e.getMessage());
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

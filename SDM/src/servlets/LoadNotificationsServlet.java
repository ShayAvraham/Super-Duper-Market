package servlets;

import com.google.gson.Gson;
import dataContainers.NoticeDataContainer;
import dataContainers.UserDataContainer;
import engineLogic.Notice;
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

public class LoadNotificationsServlet extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        UserDataContainer owner = SessionUtils.getUser(request);
        Collection<NoticeDataContainer> allNotifications = systemManager.GetUserByID(owner.getId()).getNotices();
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(allNotifications);
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

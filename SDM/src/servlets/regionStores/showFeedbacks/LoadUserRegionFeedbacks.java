package servlets.regionStores.showFeedbacks;

import com.google.gson.Gson;
import dataContainers.NoticeDataContainer;
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

public class LoadUserRegionFeedbacks extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("application/json");
        String regionName = SessionUtils.getRegionName(request);
        UserDataContainer user = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Collection<NoticeDataContainer> userRegionFeedbacks =
                systemManager.getUserRegionFeedbacks(regionName,user.getId());
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(userRegionFeedbacks);
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

package servlets;

import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import dataContainers.UserDataContainer;
import exceptions.DuplicateValuesException;
import managers.SystemManager;
import utilities.ServletUtils;
import utilities.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


public class LoginServlet extends HttpServlet
{
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        System.out.println(request.getReader().lines().collect(Collectors.joining()));
        response.setContentType("text/plain;charset=UTF-8");
        UserDataContainer userFromSession = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        if (userFromSession == null)
        {
            Gson gson = new Gson();
            UserDataContainer user = gson.fromJson(request.getReader(), UserDataContainer.class);
            synchronized (this)
            {
                try
                {
                    systemManager.AddNewUser(user);
                    request.getSession(true).setAttribute("user", user);
                    response.setStatus(200);
                    response.getOutputStream().println("pages/dashboard/dashboard.html");
                }
                catch (DuplicateValuesException e)
                {
                    response.setStatus(401);
                    response.getOutputStream().println(e.getMessage());
                }
            }
        }
        else
        {
            response.setStatus(200);
            response.getOutputStream().println("pages/dashboard/dashboard.html");
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

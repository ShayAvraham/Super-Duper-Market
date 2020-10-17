package servlets;

import dataContainers.UserDataContainer;
import managers.SystemManager;
import utilities.ServletUtils;
import utilities.SessionUtils;

import javax.management.InstanceNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet("/uploadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet
{
    private static final String SUCCESS_MSG = "File was loaded successfully";
    private static final String FILE_NOT_XML_ERROR_MSG = "File is not xml file";


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/plain;charset=UTF-8");
        Collection<Part> parts = request.getParts();
        UserDataContainer userFromSession = SessionUtils.getUser(request);
        SystemManager systemManager = ServletUtils.getSystemManager(getServletContext());
        Part part = parts.stream().findFirst().orElse(null);
        try
        {
            assert part != null;
            validateFileFormat(part);
            systemManager.LoadDataFromXMLFile(userFromSession.getId(), userFromSession.getName(), part.getInputStream());
            response.getWriter().print(SUCCESS_MSG);
        }
        catch (Exception e)
        {
            response.getWriter().print(e.getMessage());
        }
    }

    private void validateFileFormat(Part file)
    {
        if(!file.getSubmittedFileName().endsWith(".xml"))
        {
            throw new IllegalArgumentException(FILE_NOT_XML_ERROR_MSG);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}

package ru.ifmo.kot.queue.ui;

import org.eclipse.jetty.http.MimeTypes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.getWriter().write(
                "{" + "property: 'undefined'" + "}");
    }

}

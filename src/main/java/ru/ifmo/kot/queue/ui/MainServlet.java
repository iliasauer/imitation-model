package ru.ifmo.kot.queue.ui;

import org.eclipse.jetty.http.MimeTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put("property", "fuckyeah");
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.getWriter().write(json);
    }
}

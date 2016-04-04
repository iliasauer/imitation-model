package ru.ifmo.kot.queue.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.MimeTypes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RunServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String numberOfJobsParam = request.getParameter("jobs-input");
        final String numberOfWorkersParam = request.getParameter("workers-input");
        final String capacityOfStorageParam = request.getParameter("storage-input");
        final String disciplineParam = request.getParameter("discipline-select");
        final String avgIntervalParam = request.getParameter("interval-input");
        final String avgProcessingParam = request.getParameter("process-input");
        Map<String, String> map = new HashMap<>();
        map.put("property", "fuckyeah");
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.getWriter().write(json);
    }
}

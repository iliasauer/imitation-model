package ru.ifmo.kot.queue.ui;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.MimeTypes;
import ru.ifmo.kot.queue.system.QueueSystem;
import ru.ifmo.kot.queue.system.storage.StorageFactory;
import ru.ifmo.kot.queue.system.storage.StorageFactory.Discipline;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RunServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(RunServlet.class);

    private int jobs = 0;
    private int workers = 0;
    private int storage = 0;
    private Discipline discipline = null;
    private int interval = 0;
    private int process = 0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String jobsParam = request.getParameter("jobs-input");
        final String workersParam = request.getParameter("workers-input");
        final String storageParam = request.getParameter("storage-input");
        final String disciplineParam = request.getParameter("discipline-select");
        final String intervalParam = request.getParameter("interval-input");
        final String processParam = request.getParameter("process-input");
        Map<String, String> responseMap = new HashMap<>();
        if (validateParams(jobsParam, workersParam, storageParam,
                disciplineParam, intervalParam, processParam)) {
            QueueSystem.run(jobs, workers, storage, discipline, interval, process);
            QueueSystem.shutdown();
            responseMap.put("status", "Run is finished.");
        } else {
            responseMap.put("status", "Parameters are invalid.");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = mapper.writeValueAsString(responseMap);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.getWriter().write(json);;
    }

    private boolean validateParams(String ... params) {
        try {
            jobs = Integer.parseInt(params[0]);
            workers = Integer.parseInt(params[1]);
            storage = Integer.parseInt(params[2]);
            interval = Integer.parseInt(params[4]);
            process = Integer.parseInt(params[5]);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to parse parameter", e);
            return false;
        }
        if (jobs <= 0) {
            return false;
        } else if (workers <= 0) {
            return false;
        } else if (storage <= 0) {
            return false;
        } else if (interval <= 0) {
            return false;
        } else if (process <= 0) {
            return false;
        } else {
            discipline = StorageFactory.getDiscipline(params[3]);
            if (discipline == null) {
                return false;
            }
        }
        return true;
    }
}

package ru.ifmo.kot.queue.ui;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.eclipse.jetty.http.MimeTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ifmo.kot.queue.util.chart.Chart;
import ru.ifmo.kot.queue.util.chart.Charts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainServlet extends HttpServlet {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Chart> chartList = new ArrayList<>();
        chartList.add(Charts.correlationChart());
        chartList.add(Charts.prevNextChart());
        String json = MAPPER.writeValueAsString(chartList);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.getWriter().write(json);
    }
}

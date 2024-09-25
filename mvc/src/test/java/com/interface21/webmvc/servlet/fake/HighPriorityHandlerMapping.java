package com.interface21.webmvc.servlet.fake;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class HighPriorityHandlerMapping implements HandlerMapping {

    private static final Map<String, Integer> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/jazz", 1130);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return controllers.get(requestURI);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

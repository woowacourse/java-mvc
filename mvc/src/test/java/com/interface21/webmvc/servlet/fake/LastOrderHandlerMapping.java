package com.interface21.webmvc.servlet.fake;

import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class LastOrderHandlerMapping implements HandlerMapping {

    private static final Map<String, String> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/jazz", "jazz");
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return controllers.get(requestURI);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}

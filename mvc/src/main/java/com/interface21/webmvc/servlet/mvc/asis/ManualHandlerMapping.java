package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private final Map<String, Controller> controllers = new HashMap<>();

    public void register(final String path, final Controller controller) {
        controllers.put(path, controller);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        
        return controllers.get(requestURI);
    }
}

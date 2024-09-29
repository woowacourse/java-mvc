package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import samples.ManualController;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();

    public void initialize() {
        controllers.put("/manual", new ManualController());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return controllers.get(requestURI);
    }
}

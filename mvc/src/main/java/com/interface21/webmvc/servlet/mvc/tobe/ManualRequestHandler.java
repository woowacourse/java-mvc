package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class ManualRequestHandler implements RequestHandler {

    private final Object controller;
    private final Method method;

    public ManualRequestHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            return (ModelAndView) method.invoke(controller, httpRequest, httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

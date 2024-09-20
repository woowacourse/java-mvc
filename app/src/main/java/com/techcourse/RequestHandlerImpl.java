package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RequestHandlerImpl implements RequestHandler {

    private final Object controller;
    private final Method method;

    public RequestHandlerImpl(Object controller, Method method) {
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

package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(final Method method, final Object controller) {
        this.method = method;
        this.controller = controller;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return method.invoke(controller, request, response);
    }
}

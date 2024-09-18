package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HandlerExecution execution = (HandlerExecution) o;
        return Objects.equals(controller, execution.controller) && Objects.equals(method,
                execution.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controller, method);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "controller=" + controller.getClass().getName() +
                ", method=" + method +
                '}';
    }
}

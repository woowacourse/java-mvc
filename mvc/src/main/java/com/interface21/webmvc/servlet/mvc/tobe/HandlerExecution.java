package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

public class HandlerExecution {

    private final BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> targetMethod;

    public HandlerExecution(final BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> targetMethod) {
        this.targetMethod = targetMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return targetMethod.apply(request, response);
    }
}

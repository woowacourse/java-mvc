package com.techcourse.air.mvc.core.controller.tobe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import com.techcourse.air.mvc.core.returnvalue.ReturnValueHandler;
import com.techcourse.air.mvc.core.view.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final ApplicationContext context = ApplicationContextProvider.getApplicationContext();

    private final List<ReturnValueHandler> handlers;
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.handlers = new ArrayList<>();
        handlers.addAll(context.findAllBeanByType(ReturnValueHandler.class));
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object result = method.invoke(declaredObject, request, response);

        ReturnValueHandler handler = handlers.stream()
                                             .filter(rh -> rh.supportsReturnType(method))
                                             .findFirst()
                                             .orElseThrow();

        return handler.handleReturnValue(result, response);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
                "declaredObject=" + declaredObject +
                ", method=" + method +
                '}';
    }
}

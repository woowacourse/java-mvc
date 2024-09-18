package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> controller;
    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(Method method)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.method = method;
        this.controller = method.getDeclaringClass();
        this.controllerInstance = controller.getDeclaredConstructor().newInstance();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // request 의 uri, method 를 처리하는 TestController 메서드 실행
        ModelAndView modelAndView = (ModelAndView) method.invoke(controllerInstance, request, response);
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);

        return modelAndView;
    }
}

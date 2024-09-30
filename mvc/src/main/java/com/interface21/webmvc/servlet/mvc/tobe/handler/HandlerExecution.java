package com.interface21.webmvc.servlet.mvc.tobe.handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.annotation.ResponseBody;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;

public class HandlerExecution {

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        Map<String, Object> model = new HashMap<>();
        final Object responseData = method.invoke(instance, request, model);
        return convertModelAndView(responseData, model);
    }

    private ModelAndView convertModelAndView(final Object responseData, final Map<String, Object> model) {
        if (hasResponseBodyAnnotation()) {
            final Map<String, Object> data = (Map) responseData;
            final JsonView jsonView = new JsonView(data);
            return new ModelAndView(jsonView);
        }

        final String viewPath = (String) responseData;
        final JspView view = new JspView(viewPath);
        return new ModelAndView(view, model);
    }

    private boolean hasResponseBodyAnnotation() {
        return method.isAnnotationPresent(ResponseBody.class);
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object instance;
    private final Method method;

    public HandlerExecution(Method method) {
        this.instance = createInstance(method);
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object result = method.invoke(instance, request, response);
        return (ModelAndView) result;
    }

    private Object createInstance(Method method) {
        try {
            return ReflectionUtils.accessibleConstructor(method.getDeclaringClass()).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("Failed to create instance by method: {}", method.getName(), e);
            throw new RuntimeException(e);
        }
    }
}

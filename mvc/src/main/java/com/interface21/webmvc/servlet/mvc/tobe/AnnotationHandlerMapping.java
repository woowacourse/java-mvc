package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerType : controllerTypes) {
            Constructor<?> constructor = controllerType.getConstructor();
            Object handler = constructor.newInstance();
            Method[] controllerMethods = controllerType.getDeclaredMethods();

            for (Method method : controllerMethods) {
                if(!method.isAnnotationPresent(RequestMapping.class)){
                    break;
                }

                RequestMapping handlerAnnotation = method.getAnnotation(RequestMapping.class);
                String url = handlerAnnotation.value();
                RequestMethod[] httpMethods = handlerAnnotation.method();

                for (RequestMethod httpMethod : httpMethods) {
                    HandlerKey handlerKey = new HandlerKey(url, httpMethod);

                    HandlerExecution handlerExecution = new HandlerExecution((request, response) -> {
                        return (ModelAndView) method.invoke(handler, request, response);
                    });

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }

    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}

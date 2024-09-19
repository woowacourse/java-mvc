package com.interface21.webmvc.servlet.mvc.tobe;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        reflections.get(SubTypes.of(TypesAnnotated.with(Controller.class)).asClass())
                .stream()
                .forEach(clazz -> Arrays.stream(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> putHandlerExecutions(clazz, method))
                );
    }

    private void putHandlerExecutions(Class<?> clazz, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mappingUrl = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> putHandlerExecution(mappingUrl, requestMethod, clazz, method));
    }

    private void putHandlerExecution(String mappingUrl, RequestMethod requestMethod, Class<?> clazz, Method method) {
        HandlerKey handlerKey = new HandlerKey(mappingUrl, requestMethod);
        HandlerExecution handlerExecution = (request, response) -> {
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                return (ModelAndView) method.invoke(instance, request, response);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        };

        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

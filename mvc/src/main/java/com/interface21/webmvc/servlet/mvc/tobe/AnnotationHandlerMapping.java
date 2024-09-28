package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.ReflectionUtils;
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

        Map<Class<?>, Object> controllers = new ControllerScanner(basePackage).getControllers();

        controllers.forEach((controller, controllerInstance) ->
                ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class))
                        .forEach(method -> putHandlerExecutions(controller, method))
        );
    }

    private void putHandlerExecutions(final Class<?> clazz, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String mappingUrl = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        Arrays.stream(requestMethods)
                .forEach(requestMethod -> putHandlerExecution(mappingUrl, requestMethod, clazz, method));
    }

    private void putHandlerExecution(final String mappingUrl, final RequestMethod requestMethod,
                                     final Class<?> clazz, final Method method) {
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

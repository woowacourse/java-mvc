package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        Arrays.stream(basePackage).forEach(pkg -> {
            final var reflections = new Reflections(pkg.toString());

            var controllers = reflections.getTypesAnnotatedWith(Controller.class);

            controllers.forEach(clazz -> {
                try {
                    Object controller = clazz.getDeclaredConstructor().newInstance();
                    Arrays.stream(clazz.getDeclaredMethods())
                            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                            .forEach(method -> {
                                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                                for (RequestMethod httpMethod : annotation.method()) {
                                    HandlerKey handlerKey = new HandlerKey(annotation.value(), httpMethod);
                                    handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
                                }

                                log.info("value: {} -> method: {}", annotation.value(), annotation.method());
                            });
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });

            log.info("Controllers : {}", controllers);
        });
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.info("Request URI : {}", requestURI);

        String method = request.getMethod();
        log.info("Request Method : {}", method);
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}

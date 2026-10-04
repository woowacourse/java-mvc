package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        Method[] handlerMethods;
        for (Class<?> controllerClass : controllerClasses) {
            handlerMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .toArray(Method[]::new);
            try {
                Object controller = controllerClass.getConstructor().newInstance();
                log.info(Arrays.toString(handlerMethods));
                for (Method handlerMethod : handlerMethods) {
                    RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }
                    registerHandlerExecutions(url, requestMethods, handlerMethod, controller);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("리플렉션 작업 중 예외 발생: " + controllerClass.getName(), e);
            }
        }
    }

    private void registerHandlerExecutions(String url, RequestMethod[] requestMethods, Method handlerMethod,
                                           Object controller) {
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(url, requestMethod),
                    new HandlerExecution(handlerMethod, controller));
            log.info("{} {} -> {}", requestMethod, url, handlerMethod);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUrl, RequestMethod.valueOf(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}

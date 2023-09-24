package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initializing AnnotationHandlerMapping starts!");
        registerFromBasePackage(basePackages);
        log.info("Initializing AnnotationHandlerMapping succeeds!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported request method.");
            throw e;
        }
    }

    public void registerFromBasePackage(Object[] basePackages) {
        AnnotationScanner annotationScanner = new AnnotationScanner(new Reflections(basePackages));
        Map<Class<?>, Object> controllers = annotationScanner.getAnnotatedClasses(Controller.class);

        for (Class<?> handler : controllers.keySet()) {
            Object instance = controllers.get(handler);
            List<Method> methods = getRequestMappingMethods(handler);
            register(instance, methods);
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> handler) {
        return Arrays.stream(handler.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void register(Object instance, List<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            if (isInvalidRequestMapping(url, requestMethods)) {
                continue;
            }
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info("uri = {}, method = {}, name = {} is mapped!", url, requestMethod.name(), method.getName());
            }
        }
    }

    private boolean isInvalidRequestMapping(String url, RequestMethod[] requestMethods) {
        return url.isBlank() || requestMethods.length == 0;
    }
}

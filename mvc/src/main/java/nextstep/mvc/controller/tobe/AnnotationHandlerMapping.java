package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            Reflections reflections = new Reflections(basePackage);
            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                final Method[] methods = controllerClass.getMethods();
                initializeHandlerExecutions(controllerClass.getDeclaredConstructor().newInstance(), methods);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        handlerExecutions.keySet().forEach(handlerKey -> log.info(handlerKey.toString()));
    }

    private void initializeHandlerExecutions(Object controller, Method[] methods) {
        for (Method method : methods) {
            checkMethod(controller, method);
        }
    }

    private void checkMethod(Object controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final RequestMethod[] requestMethods = requestMapping.method();
            final String path = requestMapping.value();
            addHandlerExecution(controller, method, requestMethods, path);
        }
    }

    private void addHandlerExecution(Object controller, Method method, RequestMethod[] requestMethods, String path) {
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(path, requestMethod),
                    new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
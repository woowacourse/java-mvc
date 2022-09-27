package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            final Set<Method> methods = getRequestMappingMethods(clazz);
            methods.forEach(method -> addHandlerExecution(method, controllers.get(clazz)));
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void addHandlerExecution(Method method, Object instance) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution execution = new HandlerExecution(method, instance);
            handlerExecutions.put(handlerKey, execution);
        }
    }
}

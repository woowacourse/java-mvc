package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.ObjectFactory;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Map<Class<?>, Object> handlers;

    public AnnotationHandlerMapping(Object basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.handlers = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        mapHandlerWithClass();
        mapHandlerExecution();
    }

    private void mapHandlerWithClass() {
        final Set<Class<?>> handlerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (Class<?> handlerClass : handlerClasses) {
            final Object handler = new ObjectFactory<>(handlerClass).newInstance();
            handlers.put(handlerClass, handler);
        }
    }

    private void mapHandlerExecution() {
        for (Class<?> handlerClasses : handlers.keySet()) {
            final Set<Method> methods = ReflectionUtils.getAllMethods(handlerClasses, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : methods) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
                final Object handler = handlers.get(method.getDeclaringClass());
                handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
            }
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

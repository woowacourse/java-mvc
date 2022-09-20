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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {

        log.info("Initialized AnnotationHandlerMapping!");
        findHandlerFromPackage();
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void findHandlerFromPackage() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        findMethodFromClass(classes);
    }

    private void findMethodFromClass(Set<Class<?>> classes) {
        for (final Class<?> clazz : classes) {
            final Method[] methods = clazz.getMethods();
            findRequestMappingFromMethod(methods);
        }
    }

    private void findRequestMappingFromMethod(Method[] methods) {
        for (final Method method: methods) {
            addRequestMapping(method);
        }
    }

    private void addRequestMapping(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : requestMapping.method()) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                final HandlerExecution execution = new HandlerExecution(method);
                handlerExecutions.put(handlerKey, execution);
            }
        }
    }
}

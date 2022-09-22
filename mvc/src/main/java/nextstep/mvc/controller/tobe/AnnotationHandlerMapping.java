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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Class<?>> handlers = findHandlerFromPackage();
        handlers.stream()
                .map(this::findMethodFromHandler)
                .flatMap(List::stream)
                .filter(this::hasRequestMapping)
                .forEach(this::addHandlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUrl = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private Set<Class<?>> findHandlerFromPackage() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> findMethodFromHandler(Class<?> handlerClass) {
        return Arrays.stream(handlerClass.getMethods())
                .collect(Collectors.toList());
    }

    private boolean hasRequestMapping(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void addHandlerExecution(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution execution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, execution);
        }
    }
}

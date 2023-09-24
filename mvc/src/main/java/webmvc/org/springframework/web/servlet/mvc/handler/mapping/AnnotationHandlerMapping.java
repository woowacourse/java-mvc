package webmvc.org.springframework.web.servlet.mvc.handler.mapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import context.org.springframework.context.ApplicationContextAware;

public class AnnotationHandlerMapping extends ApplicationContextAware implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping() {
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        createHandlerExecution();
    }

    private void createHandlerExecution() {
        final List<Object> handlerExecutions = getApplicationContext().getBeansOfAnnotationType(Controller.class);
        for (final Object handlerExecution : handlerExecutions) {
            final List<Method> methods = getMethodsWithAnnotation(handlerExecution.getClass());
            putHandlerExecutionsByMethods(handlerExecution, methods);
        }
    }

    private List<Method> getMethodsWithAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void putHandlerExecutionsByMethods(final Object object, final List<Method> methods) {
        for (final Method method : methods) {
            putHandlerExecutionsByMethod(object, method);
        }
    }

    private void putHandlerExecutionsByMethod(final Object object, final Method method) {
        final Map<HandlerKey, HandlerExecution> handlerExecutionsByMethod =
            convertHandlerExecutions(object, method);

        handlerExecutions.putAll(handlerExecutionsByMethod);
    }

    private Map<HandlerKey, HandlerExecution> convertHandlerExecutions(final Object object, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestURI = requestMapping.value();
        final List<RequestMethod> requestMethods = List.of(requestMapping.method());

        return requestMethods.stream()
            .map(requestMethod -> new HandlerKey(requestURI, requestMethod))
            .collect(Collectors.toMap(
                handlerKey -> handlerKey,
                handlerKey -> new HandlerExecution(object, method)
            ));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = makeHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey makeHandlerKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(requestURI, requestMethod);
    }
}

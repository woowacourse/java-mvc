package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        addAllHandlerExecutions(controllers);
    }

    private void addAllHandlerExecutions(final Map<Class<?>, Object> handlers) {
        for (Map.Entry<Class<?>, Object> handlerEntry : handlers.entrySet()) {
            final List<Method> methods = getMethodsAnnotatedWithRequestMapping(handlerEntry.getKey());
            addHandlerMethods(handlerEntry.getValue(), methods);
        }
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(final Class<?> handlerClass) {
        final Method[] declaredMethods = handlerClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerMethods(final Object handler, final List<Method> methods) {
        for (Method method : methods) {
            final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = mapHandlerKeys(annotation.value(), annotation.method());
            for (HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
            }
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

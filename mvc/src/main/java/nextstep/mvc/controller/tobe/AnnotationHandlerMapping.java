package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.support.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;

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
        final Set<Method> allMethods = getRequestMappingMethods(controllers.keySet());
        for (Method method : allMethods) {
            addHandlerExecutions(controllers, method);
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> handlerClasses) {
        final Set<Method> allMethods = new HashSet<>();
        for (Class<?> handlerClass : handlerClasses) {
            final Set<Method> handlerMethods = ReflectionUtils.getAllMethods(handlerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            allMethods.addAll(handlerMethods);
        }
        return allMethods;
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method) {
        final Object handler = controllers.get(method.getDeclaringClass());
        final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        final List<HandlerKey> handlerKeys = mapHandlerKeys(annotation.value(), annotation.method());
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
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

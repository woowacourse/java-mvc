package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);
        for (Class<?> aClass : controllers.keySet()) {
            addHandlerExecutions(controllers, aClass);
        }
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Class<?> aClass) {
        for (Method method : getRequestMappingMethods(aClass)) {
            addHandlerExecution(controllers.get(aClass), method);
        }
    }

    private static Set<Method> getRequestMappingMethods(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Object instance, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        for (HandlerKey handlerKey : HandlerKey.from(requestMapping)) {
            this.handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        getAnnotatedClasses().forEach(this::registerMethods);
    }

    private Set<Class<?>> getAnnotatedClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerMethods(final Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerMethod(clazz, method);
            }
        }
    }

    private void registerMethod(final Class<?> clazz, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String value = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutions.put(
                    new HandlerKey(value, requestMethod),
                    new HandlerExecution(clazz, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.keySet().stream()
                .filter(handlerKey::equals)
                .map(handlerExecutions::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 요청입니다."));
    }
}

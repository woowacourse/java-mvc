package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.CreateHandlerInstanceException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
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
        final Set<Class<?>> targetClasses = scanPackage();
        addAllHandlerExecutions(targetClasses);
    }

    private Set<Class<?>> scanPackage() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void addAllHandlerExecutions(final Set<Class<?>> targetClasses) {
        for (Class<?> targetClass : targetClasses) {
            final List<Method> methods = getMethodsAnnotatedWithRequestMapping(targetClass);
            for (Method method : methods) {
                final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
                putHandlerExecutions(targetClass, method, annotation);
            }
        }
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(Class<?> targetClass) {
        final Method[] declaredMethods = targetClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(this::isAnnotatedWithRequestMapping)
                .collect(Collectors.toList());
    }

    private boolean isAnnotatedWithRequestMapping(final Method method) {
        return method.getDeclaredAnnotation(RequestMapping.class) != null;
    }

    private void putHandlerExecutions(final Class<?> targetClass, final Method method, final RequestMapping annotation) {
        final String url = annotation.value();
        final RequestMethod[] requestMethods = annotation.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(makeHandler(targetClass), method));
        }
    }

    private Object makeHandler(final Class<?> targetClass) {
        try {
            return targetClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new CreateHandlerInstanceException();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import web.org.springframework.web.bind.annotation.CustomRequestMappings;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private final Reflections reflections;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public RequestMappingHandlerMapping(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> handlerClass : handlerClasses) {
            String prefix = extractPathPrefix(handlerClass);
            scanHandlerMethods(prefix, handlerClass);
        }
    }

    private String extractPathPrefix(Class<?> handlerClass) {
        if (handlerClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = handlerClass.getDeclaredAnnotation(RequestMapping.class);
            return requestMapping.value();
        }
        return "";
    }

    private void scanHandlerMethods(String prefix, Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();
        for (Method method : methods) {
            scanRequestMapping(prefix, method);
        }
    }

    private void scanRequestMapping(String prefix, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            addHandlerExecutionWhenRequestMapping(prefix, requestMapping, method);
            return;
        }
        scanCustomRequestMapping(prefix, method);
    }

    private void addHandlerExecutionWhenRequestMapping(String prefix, Annotation annotation, Method method) {
        String requestPath = prefix + extractValueField(annotation);
        RequestMethod[] requestMethods = extractMethodField(annotation);

        addHandlerExecution(method, requestPath, requestMethods);
    }

    private void scanCustomRequestMapping(String prefix, Method method) {
        Arrays.stream(method.getDeclaredAnnotations())
                .filter(CustomRequestMappings::isAnyMatch)
                .findFirst()
                .ifPresent(annotation -> addHandlerExecutionWhenCustomMapping(prefix, annotation, method));
    }

    private void addHandlerExecutionWhenCustomMapping(String prefix, Annotation annotation, Method method) {
        RequestMapping requestMapping = annotation.annotationType()
                .getDeclaredAnnotation(RequestMapping.class);

        String requestPath = prefix + extractValueField(annotation);
        RequestMethod[] requestMethods = extractMethodField(requestMapping);

        addHandlerExecution(method, requestPath, requestMethods);
    }

    private void addHandlerExecution(Method method, String requestPath, RequestMethod[] requestMethods) {
        HandlerExecution handlerExecution = new HandlerExecution(method);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestPath, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] extractMethodField(Annotation annotation) {
        try {
            return (RequestMethod[]) annotation.annotationType()
                    .getDeclaredMethod("method")
                    .invoke(annotation);
        } catch (IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new AnnotationMethodInvokeException("어노테이션의 메소드를 실행시키는 도중 예외가 발생했습니다.", e);
        }
    }

    private String extractValueField(Annotation annotation) {
        try {
            return (String) annotation.annotationType()
                    .getDeclaredMethod("value")
                    .invoke(annotation);
        } catch (IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new AnnotationMethodInvokeException("어노테이션의 메소드를 실행시키는 도중 예외가 발생했습니다.", e);
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

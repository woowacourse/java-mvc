package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<RequestMapping> METHOD_ANNOTATION = RequestMapping.class;
    private static final Class<Controller> CLASS_ANNOTATION = Controller.class;

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        AnnotationScanner annotationScanner = new AnnotationScanner(basePackages);
        Map<Class<?>, Object> classWithInstance = annotationScanner.findAnnotatedClassWithInstance(CLASS_ANNOTATION);
        addHandlerExecution(classWithInstance);
    }

    private void addHandlerExecution(Map<Class<?>, Object> classWithInstance) {
        var methods = findRequestMappingMethods(classWithInstance.keySet());
        for (Method method : methods) {
            addHandlerExecutionByMethod(classWithInstance, method);
        }
    }

    private Set<Method> findRequestMappingMethods(Set<Class<?>> classes) {
        return classes.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(METHOD_ANNOTATION))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutionByMethod(Map<Class<?>, Object> classWithInstance, Method method) {
        var handler = classWithInstance.get(method.getDeclaringClass());
        var handlerExecution = new HandlerExecution(handler, method);
        addHandlerExecutionByAnnotation(method.getAnnotation(METHOD_ANNOTATION), handlerExecution);
    }

    private void addHandlerExecutionByAnnotation(RequestMapping requestMapping, HandlerExecution handlerExecution) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}

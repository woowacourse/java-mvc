package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> clazz = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : clazz) {
            initializeControllerMappings(aClass);
        }
    }

    private void initializeControllerMappings(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method[] methods = clazz.getMethods();
        Object object = clazz.getConstructor().newInstance();
        processControllerMethods(methods, object);
    }

    private void processControllerMethods(Method[] methods, Object object) {
        for (Method method : methods) {
            processMethodAnnotations(object, method, method.getAnnotations());

        }
    }

    private void processMethodAnnotations(Object object, Method method, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            handleRequestMappingAnnotation(object, method, annotation);
        }
    }

    private void handleRequestMappingAnnotation(Object object, Method method, Annotation annotation) {
        if (annotation.annotationType().equals(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            processControllerMethods(object, method, requestMapping);
        }
    }

    private void processControllerMethods(Object object, Method method, RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(object, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}

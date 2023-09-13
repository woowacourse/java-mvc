package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMappingMethods;
import web.org.springframework.web.bind.annotation.RequestMappings;
import web.org.springframework.web.bind.annotation.RequestMethod;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> handlerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> handlerClazz : handlerClazzSet) {
            setupHandlerExecutions(handlerClazz);
        }
    }

    private void setupHandlerExecutions(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (RequestMappings.hasAnyOfRequestMappings(method)) {
                setupHandlerExecutionForMethod(clazz, method);
            }
        }
    }

    private void setupHandlerExecutionForMethod(Class<?> clazz, Method method) {
        Annotation requestMapping = extractRequestMapping(method);
        Class<? extends Annotation> annotationType = requestMapping.annotationType();

        String requestURI = extractRequestURI(requestMapping);
        RequestMethod requestMethod = RequestMappingMethods.getMethod(annotationType);

        HandlerExecution handlerExecution = new HandlerExecution(instantiate(clazz), method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private static String extractRequestURI(Annotation requestMapping) {
        Class<? extends Annotation> annotationType = requestMapping.annotationType();
        try {
            Method valueMethod = annotationType.getDeclaredMethod("value");
            return (String) valueMethod.invoke(requestMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
    }

    private Annotation extractRequestMapping(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(declaredAnnotation -> RequestMappings.contains(declaredAnnotation.annotationType()))
                .findFirst()
                .orElseThrow(RequestMappingDuplicatedException::new);
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new InstantiationFailedException();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

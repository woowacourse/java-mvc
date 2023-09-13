package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
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
            Annotation[] annotations = method.getDeclaredAnnotations();
            setupHandlerExecutionIfRequestMappingAnnotationExists(clazz, method, annotations);
        }
    }

    private void setupHandlerExecutionIfRequestMappingAnnotationExists(Class<?> clazz, Method method, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (RequestMappings.isAnyMatch(annotation)) {
                setupHandlerExecution(clazz, method);
            }
        }
    }

    private void setupHandlerExecution(Class<?> clazz, Method method) {
        Annotation requestMappingAnnotation = extractRequestMappingAnnotation(method);
        Class<? extends Annotation> annotationType = requestMappingAnnotation.annotationType();

        if (annotationType.isInstance(RequestMapping.class)) {
            setupWhenAnnotationTypeIsRequestMapping(clazz, method);
            return;
        }
        setupWhenAnnotationTypeIsExtendedRequestMapping(clazz, method);
    }

    private void setupWhenAnnotationTypeIsRequestMapping(Class<?> clazz, Method method) {
        Annotation requestMappingAnnotation = extractRequestMappingAnnotation(method);

        String requestURI = extractRequestURI(requestMappingAnnotation);
        RequestMethod[] requestMethods = extractHttpMethod(requestMappingAnnotation);

        addHandlerMapping(instantiate(clazz), method, requestMethods, requestURI);
    }

    private void setupWhenAnnotationTypeIsExtendedRequestMapping(Class<?> clazz, Method method) {
        Annotation requestMappingAnnotation = extractRequestMappingAnnotation(method);
        Annotation metaRequestMappingAnnotation = extractMetaRequestMappingAnnotation(requestMappingAnnotation);

        String requestURI = extractRequestURI(requestMappingAnnotation);
        RequestMethod[] requestMethods = extractHttpMethod(metaRequestMappingAnnotation);

        addHandlerMapping(instantiate(clazz), method, requestMethods, requestURI);
    }

    private void addHandlerMapping(Object clazz, Method method, RequestMethod[] requestMethods, String requestURI) {
        HandlerExecution handlerExecution = new HandlerExecution(clazz, method);
        for (RequestMethod each : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestURI, each);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Annotation extractMetaRequestMappingAnnotation(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Annotation[] metaAnnotations = annotationType.getDeclaredAnnotations();

        return Arrays.stream(metaAnnotations)
                .filter(metaAnnotation -> metaAnnotation.annotationType().equals(RequestMapping.class))
                .findFirst()
                .orElseThrow();
    }

    private Annotation extractRequestMappingAnnotation(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(RequestMappings::isAnyMatch)
                .findFirst()
                .orElseThrow(RequestMappingNotFoundException::new);
    }

    private static String extractRequestURI(Annotation requestMapping) {
        Class<? extends Annotation> annotationType = requestMapping.annotationType();
        try {
            Method method = annotationType.getDeclaredMethod("value");
            return (String) method.invoke(requestMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
    }

    private static RequestMethod[] extractHttpMethod(Annotation requestMapping) {
        Class<? extends Annotation> annotationType = requestMapping.annotationType();
        try {
            Method method = annotationType.getDeclaredMethod("method");
            return (RequestMethod[]) method.invoke(requestMapping);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RequestMappingPathNotProvidedException();
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

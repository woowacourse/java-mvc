package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.CustomRequestMappings;
import web.org.springframework.web.bind.annotation.RequestMapping;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> handlerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> handlerClass : handlerClasses) {
            addHandlerMappings(handlerClass);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerMappings(Class<?> handlerClass) {
        Method[] methods = handlerClass.getDeclaredMethods();

        for (Method method : methods) {
            addHandlerMappingIfRequestMappingAnnotationExists(handlerClass, method);
        }
    }

    private void addHandlerMappingIfRequestMappingAnnotationExists(Class<?> clazz, Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            if (annotationType.equals(RequestMapping.class)) {
                addWhenAnnotationTypeIsRequestMapping(clazz, method);
                return;
            }
            if (CustomRequestMappings.isAnyMatch(annotation)) {
                addWhenAnnotationTypeIsCustomRequestMapping(clazz, method);
            }
        }
    }

    private void addWhenAnnotationTypeIsRequestMapping(Class<?> clazz, Method method) {
        Annotation requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);

        String requestURI = RequestMappingExtractor.extractRequestURI(requestMappingAnnotation);
        RequestMethod[] requestMethods = RequestMappingExtractor.extractRequestMethod(requestMappingAnnotation);

        addHandlerExecution(instantiate(clazz), method, requestMethods, requestURI);
    }

    private void addWhenAnnotationTypeIsCustomRequestMapping(Class<?> clazz, Method method) {
        Annotation customRequestMappingAnnotation = extractCustomRequestMappingAnnotation(method);
        Annotation requestMappingAnnotation = extractRequestMappingAnnotation(customRequestMappingAnnotation);

        String requestURI = RequestMappingExtractor.extractRequestURI(customRequestMappingAnnotation);
        RequestMethod[] requestMethods = RequestMappingExtractor.extractRequestMethod(requestMappingAnnotation);

        addHandlerExecution(instantiate(clazz), method, requestMethods, requestURI);
    }

    private Annotation extractCustomRequestMappingAnnotation(Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
                .filter(CustomRequestMappings::isAnyMatch)
                .findFirst()
                .orElseThrow(RequestMappingNotFoundException::new);
    }

    private Annotation extractRequestMappingAnnotation(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        Annotation[] metaAnnotations = annotationType.getDeclaredAnnotations();

        return Arrays.stream(metaAnnotations)
                .filter(metaAnnotation -> metaAnnotation.annotationType().equals(RequestMapping.class))
                .findFirst()
                .orElseThrow(RequestMappingNotFoundException::new);
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new InstantiationFailedException();
    }

    private void addHandlerExecution(Object clazz, Method method, RequestMethod[] requestMethods, String requestURI) {
        HandlerExecution handlerExecution = new HandlerExecution(clazz, method);

        for (RequestMethod each : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestURI, each);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

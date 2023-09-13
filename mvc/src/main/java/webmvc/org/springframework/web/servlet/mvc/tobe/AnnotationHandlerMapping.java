package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int EMPTY_REQUEST_METHOD = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            final Class<?> executeClass = findClassByType(clazz);
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            makeAnnotationHandlerMapper(executeClass, declaredMethods);
        }
    }

    private Class<?> findClassByType(Class<?> clazz)  {
        try {
            return Class.forName(clazz.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("class를 찾을 수 없습니다.");
        }
    }

    private void makeAnnotationHandlerMapper(Class<?> executeClass, Method[] declaredMethods) {
        for (Method declaredMethod : declaredMethods) {
            final RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            if (annotation != null) {
                validateAnnotation(annotation);
                final HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method()[0]);
                final HandlerExecution handlerExecution = new HandlerExecution(executeClass, declaredMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private void validateAnnotation(RequestMapping annotation) {
        final RequestMethod[] method = annotation.method();
        if (method.length == EMPTY_REQUEST_METHOD) {
            throw new IllegalArgumentException("RequestMethod 값이 없습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(handlerKey);
    }
}

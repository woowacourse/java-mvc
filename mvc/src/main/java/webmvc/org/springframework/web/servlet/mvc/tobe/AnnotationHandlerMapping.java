package webmvc.org.springframework.web.servlet.mvc.tobe;

import static core.org.springframework.util.ReflectionUtils.getClassHasAnnotationWith;
import static core.org.springframework.util.ReflectionUtils.getMethodAnnotation;
import static core.org.springframework.util.ReflectionUtils.getMethodHasAnnotationWith;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_ANNOTATION = Controller.class;
    private static final Class<RequestMapping> REQUEST_MAPPING_ANNOTATION = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Set<Class<?>> controllers = getClassHasAnnotationWith(CONTROLLER_ANNOTATION, basePackage);
        final Map<Class<?>, List<Method>> classMethods = getMethodHasAnnotationWith(REQUEST_MAPPING_ANNOTATION, controllers);
        addAllMapping(classMethods);
    }

    private void addAllMapping(final Map<Class<?>, List<Method>> classMethods) {
        try {
            for (final Class<?> controllerClass : classMethods.keySet()) {
                List<Method> methods = classMethods.get(controllerClass);
                addMapping(controllerClass, methods);
            }
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            log.error("Fail to Initialize AnnotationHandlerMapping!");
        }
    }

    private void addMapping(final Class<?> controllerClass, final List<Method> classMethods) throws Exception {
        for (Method method : classMethods) {
            final RequestMapping annotation = getMethodAnnotation(method, REQUEST_MAPPING_ANNOTATION);
            final String value = annotation.value();
            final RequestMethod[] httpMethods = annotation.method();

            for (RequestMethod httpMethod : httpMethods) {
                final HandlerKey handlerKey = new HandlerKey(value, httpMethod);
                final HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);
                log.info("add mapping {}, {}", value, httpMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestPath = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestPath, requestMethod));
    }
}

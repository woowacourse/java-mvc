package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Set<Class<?>> handlerClasses = getHandlerClasses();
        putHandlerExecutionsOfHandlerClasses(handlerClasses);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutionsOfHandlerClasses(Set<Class<?>> handlerClasses) {
        for (Class<?> handlerClass : handlerClasses) {
            final Method[] methods = handlerClass.getDeclaredMethods();
            putHandlerExecutionsOfHandlerMethods(methods);
        }
    }

    private Set<Class<?>> getHandlerClasses() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void putHandlerExecutionsOfHandlerMethods(Method[] methods) {
        for (Method method : methods) {
            final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

            final String requestUri = requestMappingAnnotation.value();
            final RequestMethod requestMethod = requestMappingAnnotation.method()[0];

            final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

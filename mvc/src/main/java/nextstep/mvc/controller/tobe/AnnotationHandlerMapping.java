package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object packageName : basePackage) {
            final ReflectionUtils reflectionUtils = new ReflectionUtils(packageName);
            createHandlerKey(reflectionUtils, Controller.class, RequestMapping.class);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void createHandlerKey(ReflectionUtils reflectionUtils, Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation) {
        final Set<Class<?>> typesAnnotatedWith = reflectionUtils.getTypesAnnotatedWith(classAnnotation);

        for (Class<?> controller : typesAnnotatedWith) {
            List<Method> methods = reflectionUtils.methods(controller, methodAnnotation);

            iterateMethods(reflectionUtils, methodAnnotation, controller, methods);
        }
    }

    private void iterateMethods(ReflectionUtils reflectionUtils, Class<? extends Annotation> methodAnnotation, Class<?> controller, List<Method> methods) {
        for (Method method : methods) {
            putHandlerKey(reflectionUtils.newInstance(controller), method, methodAnnotation);
        }
    }

    private void putHandlerKey(Object instance, Method method, Class<? extends Annotation> methodAnnotation) {
        final RequestMapping annotation = (RequestMapping) method.getAnnotation(methodAnnotation);

        for (RequestMethod annotationMethod : annotation.method()) {
            final HandlerKey handlerKey = HandlerKey.of(annotation.value(), annotationMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        final HandlerKey handlerKey = HandlerKey.of(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

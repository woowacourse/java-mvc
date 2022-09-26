package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            initializeHandlerByAnnotation(entry.getKey(), entry.getValue());
        }
    }

    private void initializeHandlerByAnnotation(final Class<?> clazz, final Object instance) {
        final Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method method : allMethods) {
            addHandlerByAnnotationInMethod(instance, method);
        }
    }

    private void addHandlerByAnnotationInMethod(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        mappingHandler(instance, method, requestMapping);
    }

    private void mappingHandler(final Object instance, final Method method, final RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new RuntimeException("Not Found");
        }
        return handlerExecutions.get(handlerKey);
    }
}

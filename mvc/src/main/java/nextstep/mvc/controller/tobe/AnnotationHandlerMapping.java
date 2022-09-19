package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackages);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        initializeHandlers(controllerClasses);
    }

    private void initializeHandlers(final Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            final Method[] methods = controllerClass.getMethods();
            addHandlerExecutionsFromMethods(controllerClass, methods);
        }
    }

    private void addHandlerExecutionsFromMethods(final Class<?> controllerClass, final Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                addHandlerExecution(controllerClass, method);
            }
        }
    }

    private void addHandlerExecution(final Class<?> controllerClass, final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
        final HandlerExecution handlerExecution = new HandlerExecution(createControllerInstance(controllerClass),
                method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Object createControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException exception) {
            throw new IllegalStateException("Fail to create handler instance");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        validateHandlerExistence(handlerKey);

        return handlerExecutions.get(handlerKey);
    }

    private void validateHandlerExistence(final HandlerKey handlerKey) {
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("Handler not found");
        }
    }
}

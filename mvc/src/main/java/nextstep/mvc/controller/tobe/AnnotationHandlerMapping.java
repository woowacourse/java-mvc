package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
            final List<Method> methods = getRequestMappingMethods(controllerClass);
            addHandlerByRequestMapping(controllerClass, methods);
        }
    }

    private List<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerByRequestMapping(final Class<?> controllerClass, final List<Method> methods) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping.value(), requestMapping.method());
            final Object controller = createControllerInstance(controllerClass);
            addHandlerExecutions(handlerKeys, controller, method);
        }
    }

    private List<HandlerKey> getHandlerKeys(final String requestValue, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestValue, requestMethod))
                .collect(Collectors.toList());
    }

    private Object createControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException exception) {
            throw new IllegalStateException("Fail to create handler instance");
        }
    }

    private void addHandlerExecutions(final List<HandlerKey> handlerKeys, final Object controller,
                                      final Method method) {
        for (HandlerKey handlerKey : handlerKeys) {
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(handlerKey);
    }
}

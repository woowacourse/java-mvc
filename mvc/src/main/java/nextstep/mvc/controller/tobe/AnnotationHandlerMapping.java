package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        initializeHandlerExecutions(controllers);
        logSavedController();
    }

    private void initializeHandlerExecutions(final Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            final Object controllerInstance = getControllerInstance(controller);
            final Method[] methods = controller.getMethods();
            final List<Method> requestMethods = Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());

            saveHandlerExecutions(controllerInstance, requestMethods);
        }
    }

    private void saveHandlerExecutions(final Object controllerInstance, final List<Method> requestMethods) {
        for (Method requestMethod : requestMethods) {
            saveHandlerExecution(controllerInstance, requestMethod);
        }
    }

    private void saveHandlerExecution(final Object controllerInstance, final Method requestMethod) {
        final RequestMapping annotation = requestMethod.getAnnotation(RequestMapping.class);
        final String url = annotation.value();
        final RequestMethod[] httpMethods = annotation.method();

        for (RequestMethod httpMethod : httpMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, httpMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, requestMethod));
        }
    }

    private Object getControllerInstance(Class<?> controller) {
        try {
            final Constructor<?> constructor = controller.getConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            log.warn(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private void logSavedController() {
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Path : {}, Controller : {}", handlerKey.getUrl(),
                        handlerExecutions.get(handlerKey).getClass()));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}

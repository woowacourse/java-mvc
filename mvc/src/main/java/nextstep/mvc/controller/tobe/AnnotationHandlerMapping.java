package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.ControllerCreateException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            putHandlerExecutionOfController(controller);
        }
    }

    private void putHandlerExecutionOfController(Class<?> controller) {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            putHandlerExecutionOfMethod(controller, method);
        }
    }

    private void putHandlerExecutionOfMethod(Class<?> controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping);

            Object controllerInstance = createInstance(controller);
            HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            putHandlerExecutionOfHandlerKeys(handlerKeys, handlerExecution);
        }
    }

    private static Object createInstance(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new ControllerCreateException();
        }
    }

    private List<HandlerKey> createHandlerKeys(RequestMapping requestMapping) {
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutionOfHandlerKeys(List<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
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

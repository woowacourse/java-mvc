package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Start Initializing AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(this::setHandlerExecutionsOf);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void setHandlerExecutionsOf(Class<?> controller) {
        Method[] methods = controller.getDeclaredMethods();

        Arrays.stream(methods).forEach(method -> setHandlerExecutionsOf(controller, method));
    }

    private void setHandlerExecutionsOf(Class<?> controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return;
        }

        String requestURL = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(requestURL, requestMethod);
            handlerExecutions.put(key, getHandlerExecutionOf(controller, method));
            log.info("Path : {}, Controller : {}", key, controller);
        }
    }

    private HandlerExecution getHandlerExecutionOf(Class<?> controller, Method method) {
        try {
            Constructor<?> constructor = controller.getConstructor();
            constructor.setAccessible(true);
            return new HandlerExecution(constructor.newInstance(), method);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Handler initializing error");
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

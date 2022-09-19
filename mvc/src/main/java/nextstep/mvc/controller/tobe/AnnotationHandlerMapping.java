package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.common.exception.ErrorType;
import nextstep.mvc.common.exception.FailedHandlerMappingException;
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
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::mapHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapHandlerExecutions(final Class<?> clazz) {
        try {
            Object handler = clazz.getDeclaredConstructor().newInstance();
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecution(handler, method));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new FailedHandlerMappingException(ErrorType.FAIL_HANDLER_MAPPING);
        }
    }

    private void addHandlerExecution(final Object handler, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(key, handlerExecution);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}

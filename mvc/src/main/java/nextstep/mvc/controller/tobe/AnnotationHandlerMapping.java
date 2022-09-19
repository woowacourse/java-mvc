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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        classes.forEach(this::mappingHandlerExecutions);
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, method);

        return handlerExecutions.get(handlerKey);
    }

    private void mappingHandlerExecutions(Class<?> aClass) {
        Object instance = getNewInstance(aClass);
        for (Method method : aClass.getDeclaredMethods()) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            mappingHandlerExecution(instance, method, requestMapping);
        }
    }

    private Object getNewInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void mappingHandlerExecution(Object instance, Method method, RequestMapping requestMapping) {
        String url = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(instance, method));
        }
    }
}

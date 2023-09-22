package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            addHandlerMappings(controller);
        }
        log.info("Initialized Annotation Handler Mapping!");
    }

    private void addHandlerMappings(final Class<?> clazz) {
        Object controller = instantiate(clazz);
        String requestPath = getRequestPath(clazz);
        Method[] methods = clazz.getDeclaredMethods();
        for (final Method method : methods) {
            mapControllerMethod(controller, method, requestPath);
        }
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException("controller 인스턴스 생성 실패", e);
        }
    }

    private String getRequestPath(final Class<?> clazz) {
        RequestMapping requestMappingAnnotation = clazz.getAnnotation(RequestMapping.class);
        if (requestMappingAnnotation == null) {
            return "";
        }
        return requestMappingAnnotation.value();
    }

    private void mapControllerMethod(final Object controller, final Method method, final String requestPath) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        RequestMethod[] requestMethods = requestMapping.method();
        String url = requestPath + requestMapping.value();
        for (final RequestMethod requestMethod : requestMethods) {
            addHandlerExecution(controller, method, url, requestMethod);
        }
    }

    private void addHandlerExecution(final Object controller, final Method method, final String url,
                                     final RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        HandlerExecution execution = new HandlerExecution(controller, method);
        handlerExecutions.put(handlerKey, execution);
        log.info("Path : {}, Method: {},  Controller : {}", url, requestMethod, controller.getClass());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

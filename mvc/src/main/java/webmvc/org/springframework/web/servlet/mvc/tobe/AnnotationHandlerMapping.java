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
import webmvc.org.springframework.util.ReflectionUtils;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : annotatedControllers) {
            instantiateAndStore(clazz);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void instantiateAndStore(Class<?> clazz) {
        try {
            Object handler = instantiateHandler(clazz);
            storeAnnotatedHandlers(clazz, handler);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new SpringInitializationException("AnnotationHandlerMapping 초기화에 실패했습니다.");
        }
    }

    private Object instantiateHandler(Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ReflectionUtils.accessibleConstructor(clazz).newInstance();
    }

    private void storeAnnotatedHandlers(Class<?> clazz, Object handler) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                storeHandler(handler, method);
            }
        }
    }

    private void storeHandler(Object handler, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(
                RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        HandlerMethod handlerMethod = new HandlerMethod(handler, method);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handlerMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(),
                        RequestMethod.resolve(request.getMethod())));
    }
}

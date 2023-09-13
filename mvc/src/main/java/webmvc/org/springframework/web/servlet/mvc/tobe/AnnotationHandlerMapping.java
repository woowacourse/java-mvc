package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
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
        Set<Class<?>> annotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : annotatedWithController) {
            try {
                Object handler = instantiateHandler(clazz);
                putAnnotatedHandler(clazz, handler);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new SpringInitializationException("AnnotationHandlerMapping 초기화에 실패했습니다.");
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object instantiateHandler(Class<?> clazz)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ReflectionUtils.accessibleConstructor(clazz).newInstance();
    }

    private void putAnnotatedHandler(Class<?> clazz, Object handler) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            putAnnotatedHandlerMethods(handler, method);
        }
    }

    private void putAnnotatedHandlerMethods(Object handler, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(
                    RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                HandlerMethod handlerMethod = new HandlerMethod(handler, method);
                HandlerExecution handlerExecution = new HandlerExecution(handlerMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.resolve(request.getMethod())));
    }
}

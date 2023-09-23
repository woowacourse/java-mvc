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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : classes) {
            findAnnotatedMethod(aClass);
        }
    }

    private void findAnnotatedMethod(Class<?> aClass) {
        for (Method method : aClass.getDeclaredMethods()) {
            addHandler(aClass, method);
        }
    }

    private void addHandler(Class<?> aClass, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();
        for (RequestMethod requestMethod : methods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(getObject(aClass), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getObject(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

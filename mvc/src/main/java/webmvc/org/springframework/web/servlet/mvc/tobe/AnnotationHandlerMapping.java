package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : classes) {
            addMethods(aClass);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addMethods(Class<?> aClass) {
        for (Method method : aClass.getDeclaredMethods()) {
            addHandler(aClass, method);
        }
    }

    private void addHandler(Class<?> aClass, Method method) {
        RequestMapping rootRequestMapping = aClass.getAnnotation(RequestMapping.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String rootUrl = rootRequestMapping == null ? "" : requestMapping.value();
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(rootUrl + url, requestMethod);
            Object object = getInstance(aClass);
            handlerExecutions.put(handlerKey, new HandlerExecution(object, method));
        }
    }

    private Object getInstance(Class<?> aClass) {
        try {
            return aClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase()));
        return handlerExecutions.get(handlerKey);
    }
}

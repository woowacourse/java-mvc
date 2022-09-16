package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        findHandlerFromPackage();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void findHandlerFromPackage() {
        for (Object eachPackage : basePackage) {
            Reflections reflections = new Reflections(eachPackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            findMethodFromClass(classes);
        }
    }

    private void findMethodFromClass(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getMethods();
            findRequestMappingFromMethod(methods);
        }
    }

    private Method findRequestMappingFromMethod(Method[] methods) {
        for (Method method: methods) {
            addRequestMapping(method);
        }
        return null;
    }

    private void addRequestMapping(Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                HandlerExecution execution = new HandlerExecution(method);
                handlerExecutions.put(handlerKey, execution);
            }
        }
    }
}

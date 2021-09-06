package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);
            for (Class<?> controllerClass : controllers.keySet()) {
                Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
                addHandlerExecutions(controllerClass, methods);
            }
        } catch (Exception e) {
            log.error("Annotation Handler Mapping Fail!", e);
        }
    }

    private void addHandlerExecutions(Class<?> controllerClass, Set<Method> methods) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                HandlerExecution handlerExecution = createHandlerExecution(controllerClass, method);
                this.handlerExecutions.put(handlerKey, handlerExecution);
                log.info("Path : {}, Controller : {}", url, controllerClass.getName());
            }
        }
    }

    private HandlerExecution createHandlerExecution(Class<?> controllerClass, Method method) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Object handler = controllerClass.getConstructor().newInstance();
        return new HandlerExecution(handler, method);
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(uri, requestMethod));
    }
}

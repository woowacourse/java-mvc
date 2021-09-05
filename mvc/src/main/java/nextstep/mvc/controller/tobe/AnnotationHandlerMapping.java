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
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controllerClass : controllerClasses) {
                addHandlerExecutions(controllerClass);
            }
        } catch (Exception e) {
            LOG.error("Annotation Handler Mapping Fail!", e);
        }
        LOG.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Class<?> controllerClass) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        List<Method> methods = getMethodsWithRequestMapping(controllerClass);
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                HandlerExecution handlerExecution = getHandlerExecution(controllerClass, method);
                this.handlerExecutions.put(handlerKey, handlerExecution);
                LOG.info("Add Handler Execution Key : {}, Value : {} !", handlerKey, handlerExecution);
            }
        }
    }

    private List<Method> getMethodsWithRequestMapping(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private HandlerExecution getHandlerExecution(Class<?> controllerClass, Method method) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        Object handler = controllerClass.getConstructor().newInstance();
        return new HandlerExecution(handler, method);
    }

    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}

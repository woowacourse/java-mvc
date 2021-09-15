package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {
    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(Object[] basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<HandlerKey, HandlerExecution> getControllers() {
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiate(controllerClasses);
    }

    private Map<HandlerKey, HandlerExecution> instantiate(Set<Class<?>> classesAnnotatedWithController) {
        Map<HandlerKey, HandlerExecution> controllers = new HashMap<>();

        for (Class<?> controllerClass : classesAnnotatedWithController) {
            List<Method> methodsWithRequestMapping = getMethodsAnnotatedWithRequestMapping(controllerClass);

            for (Method method : methodsWithRequestMapping) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                createHandlerExecution(controllers, controllerClass, method, annotation);
            }
        }
        return controllers;
    }

    private void createHandlerExecution(Map<HandlerKey, HandlerExecution> controllers, Class<?> controllerClass, Method method, RequestMapping annotation) {
        try {
            String requestUri = annotation.value();
            RequestMethod[] requestMethods = annotation.method();

            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
                Object handler = controllerClass.getConstructor().newInstance();
                controllers.put(handlerKey, new HandlerExecution(handler, method));
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Exception occurred while AnnotationHandlerMapping initialization" + e.getMessage());
            throw new IllegalStateException("Handler 초기화 실패");
        }
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(Class<?> controllerClass) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                     .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }
}

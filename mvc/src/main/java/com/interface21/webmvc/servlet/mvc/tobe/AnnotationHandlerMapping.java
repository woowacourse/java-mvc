package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllerClasses = getControllerClasses();
        List<Method> methods = getRequestMappingMethods(controllerClasses);

        for (Method method : methods) {
            addHandlerExecutions(method);
        }
    }

    private Set<Class<?>> getControllerClasses() {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> getRequestMappingMethods(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(controllerClass -> Arrays.stream(controllerClass.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutions(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        Class<?> declaringClass = method.getDeclaringClass();
        Object instance = BeanFactory.getInstance(declaringClass);
        HandlerExecution handlerExecution = new HandlerExecution(instance, method);

        List<HandlerKey> handlerKeys = HandlerKey.of(requestMapping.value(), requestMapping.method());
        handlerKeys.forEach(handlerKey -> {
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Register Handler -> Method: {}#{}, URL: {}, HTTP Method: {}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    handlerKey.getUrl(),
                    handlerKey.getRequestMethod()
            );
        });
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

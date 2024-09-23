package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) throws Exception {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    public void initialize() throws Exception {
        for (Class<?> controllerClass : findControllerClasses()) {
            initializeHandlerExecutions(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        String uri = request.getRequestURI();

        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private Set<Class<?>> findControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void initializeHandlerExecutions(Class<?> controllerClass) throws Exception {
        List<Method> mapperMethods = findMapperMethods(controllerClass);
        Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();

        for (Method mapperMethod : mapperMethods) {
            addMappings(mapperMethod, controllerInstance);
        }
    }

    private List<Method> findMapperMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addMappings(Method mapperMethod, Object controllerInstance) {
        RequestMapping annotation = mapperMethod.getAnnotation(RequestMapping.class);
        String uri = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            handlerExecutions.put(handlerKey, createHandlerExecution(mapperMethod, controllerInstance));
        }
    }

    private HandlerExecution createHandlerExecution(Method mapperMethod, Object controllerInstance) {
        return new HandlerExecution() {
            @Override
            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
                return (ModelAndView) mapperMethod.invoke(controllerInstance, request, response);
            }
        };
    }
}

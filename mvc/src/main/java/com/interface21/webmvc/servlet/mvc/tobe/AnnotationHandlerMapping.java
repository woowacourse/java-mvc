package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public Object getHandler(final HttpServletRequest request) {
        System.out.println("annotaion.request.getRequestURI() = " + request.getRequestURI());
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Entry<Class<?>, Object> controllersEntry : controllers.entrySet()) {
            Class<?> controllerClass = controllersEntry.getKey();
            Object controller = controllersEntry.getValue();

            List<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);

            for (Method requestMappingMethod : requestMappingMethods) {
                RequestMapping annotation = requestMappingMethod.getAnnotation(RequestMapping.class);
                addHandlerExecutions(controller, requestMappingMethod, annotation);
            }
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> classes) {
        List<Method> methods = new ArrayList<>();
        Method[] declaredMethods = classes.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                methods.add(declaredMethod);
            }
        }
        return methods;
    }

    private void addHandlerExecutions(Object controller, Method handlerMethod, RequestMapping annotation) {
        String route = annotation.value();
        RequestMethod[] httpMethods = annotation.method();
        HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);

        List<HandlerKey> handlerKeys = mapHandlerKeys(route, httpMethods);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerKeys.add(handlerKey);
        }
        return handlerKeys;
    }
}

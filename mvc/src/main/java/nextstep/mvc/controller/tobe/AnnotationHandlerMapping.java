package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerAnnotationClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerAnnotationClasses) {
            requestMapping(controllerClass);
        }
    }

    private void requestMapping(Class<?> controllerClass) {
        List<Method> requestMappingMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());

        for(Method method : requestMappingMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            String uri = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            for(RequestMethod requestMethod : requestMethods) {
                addHandlerExecution(uri, requestMethod, controllerClass, method);
            }
        }
    }

    private void addHandlerExecution(String uri, RequestMethod requestMethod, Class<?> controllerClass, Method method) {
        try {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            Object handler = controllerClass.getConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

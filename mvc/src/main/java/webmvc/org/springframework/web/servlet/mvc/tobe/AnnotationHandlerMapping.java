package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            List<Method> requestMappingMethods = getRequestMappingMethods(controller);
            Constructor<?> constructor = getConstructor(controller);
            Object instance = makeInstance(constructor);
            addHandlerExecutions(requestMappingMethods, instance);
        }
    }

    private static List<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Constructor<?> getConstructor(Class<?> controller) {
        try {
            return controller.getConstructor();
        } catch (NoSuchMethodException e) {
            log.error("{} 의 기본 생성자가 존재하지 않습니다.", controller.getName());
            throw new RuntimeException(String.format("%s 의 기본 생성자가 존재하지 않습니다.", controller.getName()));
        }
    }

    private void addHandlerExecutions(List<Method> requestMappingMethods, Object instance) {
        for (Method requestMappingMethod : requestMappingMethods) {
            RequestMapping requestMapping = requestMappingMethod.getAnnotation(RequestMapping.class);
            RequestMethod[] controllerMethods = requestMapping.method();
            List<HandlerKey> handlerKeys = makeHandlerKeys(requestMapping, controllerMethods);
            putHandlerExecutions(instance, requestMappingMethod, handlerKeys);
        }
    }

    private Object makeInstance(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.debug("fail make to instance");
            throw new RuntimeException(e);
        }
    }

    private void putHandlerExecutions(Object instance, Method requestMappingMethod, List<HandlerKey> handlerKeys) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(requestMappingMethod, instance));
        }
    }

    private List<HandlerKey> makeHandlerKeys(RequestMapping requestMapping, RequestMethod[] controllergMethods) {
        return Arrays.stream(controllergMethods)
                .map(controllerMethod -> new HandlerKey(requestMapping.value(), controllerMethod))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        String requestURI = request.getRequestURI();

        HandlerKey requestHandlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(requestHandlerKey);
    }
}

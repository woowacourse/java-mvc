package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import web.org.springframework.web.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

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
    public boolean supports(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllerClazz = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllerClazz) {
            List<Method> requestMappingMethods = extractRequestMappingMethod(clazz);
            addHandlersByMethod(clazz, requestMappingMethods);
        }
    }

    private List<Method> extractRequestMappingMethod(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                     .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }

    private void addHandlersByMethod(Class<?> clazz, List<Method> requestMappingMethods) {
        for (Method requestMappingMethod : requestMappingMethods) {
            addHandler(clazz, requestMappingMethod, requestMappingMethod.getAnnotation(RequestMapping.class));
        }
    }

    private void addHandler(Class<?> clazz, Method handler, RequestMapping requestMapping) {
        HandlerExecution handlerExecution = new HandlerExecution(clazz, handler);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new HandlerNotFoundException("해당 URI와 Method에 대한 요청을 처리할 수 없습니다.");
    }
}

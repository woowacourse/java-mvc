package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initializing AnnotationHandlerMapping starts!");
        for (Object base : basePackage) {
            ControllerScanner controllerScanner = new ControllerScanner(new Reflections(base));
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            Set<Method> methods = getRequestMappingMethods(controllers.keySet());
            for (Method method : methods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                addHandlerExecutions(controllers, method, requestMapping);
            }
        }
        log.info("Initializing AnnotationHandlerMapping succeeds!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported request method.");
            return null;
        }
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        Object instance = controllers.get(method.getDeclaringClass());
        HandlerExecution handlerExecution = new HandlerExecution(instance, method);
        RequestMethod[] requestMethods = requestMapping.method();
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMethods);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        return controllers
                .stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }
}

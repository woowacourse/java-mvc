package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> instances = createControllerInstance(controllers);
        List<Method> requestMappingMethods = mapRequestMappingMethods(controllers);

        putHandlerExecutions(requestMappingMethods, instances);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Map<Class<?>, Object> createControllerInstance(Set<Class<?>> controllers) {
        try {
            Map<Class<?>, Object> instances = new HashMap<>();
            for (Class<?> controller : controllers) {
                instances.put(controller, controller.newInstance());
            }
            return instances;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<Method> mapRequestMappingMethods(Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getDeclaredMethods).flatMap(Stream::of)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutions(List<Method> requestMappingMethods, Map<Class<?>, Object> instances) {
        for (Method method : requestMappingMethods) {
            Object controller = instances.get(method.getDeclaringClass());
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String path = annotation.value();
            RequestMethod[] requestMethods = annotation.method();
            RequestMethod requestMethod = RequestMethod.valueOf(requestMethods[0].name());
            handlerExecutions.put(new HandlerKey(path, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

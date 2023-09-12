package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.stream.Collectors.toList;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            initializeController(controller);
        }
    }

    private void initializeController(Class<?> controller) {
        Object instance = getInstance(controller);
        List<Method> methods = getAnnotatedMethod(controller);
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            List<HandlerKey> handlerKeys = getHandlerKeys(annotation);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            putHandlerExecution(handlerKeys, handlerExecution);
        }
    }

    private Object getInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<Method> getAnnotatedMethod(Class<?> controller) {
        Method[] methods = controller.getMethods();
        return Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(toList());
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping annotation) {
        String url = annotation.value();
        RequestMethod[] httpMethods = annotation.method();
        return Arrays.stream(httpMethods)
            .map(httpMethod -> new HandlerKey(url, httpMethod))
            .collect(toList());
    }

    private void putHandlerExecution(List<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.NotFoundHandlerException;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        initHandlerExecutions();
    }

    private void initHandlerExecutions() throws Exception {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Method> methods = getRequestMappingMethods(controllers);
        for (Method method : methods) {
            Class<?> controllerClass = method.getDeclaringClass();
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String url = annotation.value();
            putHandlerExecutions(method, controllerClass, annotation, url);
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllers) {
        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void putHandlerExecutions(final Method method, final Class<?> controllerClass,
                                      final RequestMapping annotation, final String url) throws Exception {
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass.getDeclaredConstructor().newInstance(), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String methodName = request.getMethod();
        RequestMethod requestMethod = RequestMethod.findRequestMethod(methodName);
        String url = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new NotFoundHandlerException();
        }
        return handlerExecution;
    }
}

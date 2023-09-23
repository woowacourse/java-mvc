package webmvc.org.springframework.web.servlet.mvc.handler;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handler;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handler = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        controllerClasses.forEach(this::addMethodsOnHandler);
    }

    private void addMethodsOnHandler(final Class<?> controllerClass) {
        final Method[] methods = controllerClass.getDeclaredMethods();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addMethodOnHandler(controllerClass, method));
    }

    private void addMethodOnHandler(final Class<?> controllerClass, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String path = controllerClass.getAnnotation(Controller.class).path() + requestMapping.value();
        final HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);

        Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .forEach(handlerKey -> handler.put(handlerKey, handlerExecution));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handler.get(handlerKey);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private HandlerExecution defaultHandlerExecution;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        defaultHandlerExecution = makeDefaultHandlerExecution();
        final Reflections reflections = new Reflections(basePackage);
        final List<Method> methods = reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
        for (Method method : methods) {
            putHandlerMethodPerRequestMethod(method);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private HandlerExecution makeDefaultHandlerExecution() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        final Method defaultHandler = DefaultHandler.class
                .getDeclaredMethod(
                        "defaultHandler",
                        HttpServletRequest.class,
                        HttpServletResponse.class
                );
        return new HandlerExecution(defaultHandler, defaultHandler.getDeclaringClass().newInstance());
    }

    private void putHandlerMethodPerRequestMethod(final Method handler) throws IllegalAccessException, InstantiationException {
        final RequestMapping annotation = handler.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = annotation.method();
        for (RequestMethod requestMethod :
                requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(annotation.value(), requestMethod),
                    new HandlerExecution(handler, handler.getDeclaringClass().newInstance())
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.getOrDefault(handlerKey, defaultHandlerExecution);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private HandlerExecution defaultHandlerExecution;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() throws NoSuchMethodException, InstantiationException, IllegalAccessException {
        defaultHandlerExecution = makeDefaultHandlerExecution();
        final Reflections reflections = new Reflections(basePackage);
        for (Class<?> clazz : reflections.getTypesAnnotatedWith(context.org.springframework.stereotype.Controller.class)) {
            putHandlerMethodPerClass(clazz);
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

    private void putHandlerMethodPerClass(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
        for (Method method : methods) {
            putHandlerMethodPerRequestMethod(clazz, method);
        }
    }

    private void putHandlerMethodPerRequestMethod(final Class<?> clazz, final Method handler) throws IllegalAccessException, InstantiationException {
        final RequestMapping classAnnotation = clazz.getAnnotation(RequestMapping.class);
        final RequestMapping methodAnnotation = handler.getAnnotation(RequestMapping.class);

        final String path = getHandlerPath(classAnnotation, methodAnnotation);
        final List<RequestMethod> requestMethods = getHandlerRequestMethods(classAnnotation, methodAnnotation);
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(path, requestMethod),
                    new HandlerExecution(handler, handler.getDeclaringClass().newInstance())
            );
        }
    }

    private String getHandlerPath(final RequestMapping classAnnotation, final RequestMapping methodAnnotation) {
        if (classAnnotation != null) {
            return classAnnotation.value() + methodAnnotation.value();
        }
        return methodAnnotation.value();
    }

    private List<RequestMethod> getHandlerRequestMethods(final RequestMapping classAnnotation, final RequestMapping methodAnnotation) {
        final List<RequestMethod> requestMethods = new ArrayList<>();
        if (classAnnotation != null) {
            requestMethods.addAll(List.of(classAnnotation.method()));
        }
        requestMethods.addAll(List.of(methodAnnotation.method()));
        return requestMethods;
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return new AnnotationHandler(handlerExecutions.getOrDefault(handlerKey, defaultHandlerExecution));
    }
}

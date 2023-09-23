package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
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

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, Handler> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);

        for (final Class<?> controllerType : reflections.getTypesAnnotatedWith(Controller.class)) {
            final Object controller = getInstance(controllerType);
            final List<Method> controllerMethods = getMethods(controllerType);

            controllerMethods.forEach(method -> addHandlerExecutions(method, controller));
        }

        handlerExecutions.keySet().forEach(handlerKey -> log.info("{}", handlerKey));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object getInstance(final Class<?> classType) {
        try {
            return classType.getConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Method> getMethods(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final Method method, final Object controller) {
        final RequestMapping request = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        final AnnotationHandler handler = new AnnotationHandler(handlerExecution);

        Arrays.stream(request.method())
                .map(httpMethod -> new HandlerKey(request.value(), httpMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handler));
    }

    @Override
    public boolean supports(final HttpServletRequest request) {
        final HandlerKey handlerKey = parseToKey(request);
        return handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = parseToKey(request);

        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey parseToKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return new HandlerKey(requestURI, requestMethod);
    }
}

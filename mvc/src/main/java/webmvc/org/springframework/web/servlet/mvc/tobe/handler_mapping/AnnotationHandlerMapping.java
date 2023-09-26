package webmvc.org.springframework.web.servlet.mvc.tobe.handler_mapping;

import static java.util.Map.entry;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.exception.RequestMethodNotValidException;
import webmvc.org.springframework.web.servlet.mvc.tobe.default_controller.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.Handler;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final String DEFAULT_CONTROLLER_PACKAGE =
        ForwardController.class.getPackage().getName();
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initializeByPackages(DEFAULT_CONTROLLER_PACKAGE);
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        initializeByPackages(basePackage);
    }

    private void initializeByPackages(final Object... basePackage) {
        final Set<Class<?>> controllerClazz = new Reflections(basePackage)
            .getTypesAnnotatedWith(Controller.class);
        final Map<HandlerKey, HandlerExecution> handlerExecutions = controllerClazz
            .stream()
            .map(Class::getMethods)
            .flatMap(Arrays::stream)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .flatMap(this::mapExecutions)
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        this.handlerExecutions.putAll(handlerExecutions);
    }

    private Stream<Entry<HandlerKey, HandlerExecution>> mapExecutions(
        final Method handleTo
    ) {
        final RequestMapping requestMapping = handleTo.getAnnotation(RequestMapping.class);

        final String path = requestMapping.value();
        final RequestMethod[] methods = requestMapping.method();

        return Arrays.stream(methods)
            .map(method -> new HandlerKey(path, method))
            .map(key -> entry(key, new HandlerExecution(handleTo)));
    }

    @Override
    public Handler getHandler(final HttpServletRequest request) {
        final String servletPath = request.getRequestURI();
        try {
            final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            final HandlerKey handlerKey = new HandlerKey(servletPath, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (final IllegalArgumentException e) {
            throw new RequestMethodNotValidException();
        }
    }
}

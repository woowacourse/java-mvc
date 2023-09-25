package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private HandlerExecution notFoundExecution;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClass : controllerClasses) {
            initializeController(controllerClass);
        }
    }

    private void initializeController(Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();
            final Set<Method> handlerMethods = getHandlerMethods(controllerClass);

            for (final Method method : handlerMethods) {
                putHandler(controller, method);
            }
            log.info("Load Controller - {}", controllerClass.getSimpleName());
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e
        ) {
            final String errorMessage = "Fail Initializing Controller - {}" + controllerClass.getSimpleName();
            log.error(errorMessage, e);
            throw new RuntimeException(errorMessage);
        }
    }

    private Set<Method> getHandlerMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    private void putHandler(final Object controller, final Method method) {
        final RequestMapping requestMappingInfo = method.getAnnotation(RequestMapping.class);

        for (RequestMethod requestMethod : requestMappingInfo.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMappingInfo.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public void setNotFoundModelAndView(final ModelAndView modelAndView) {
        this.notFoundExecution = new ForwardingExecution(modelAndView);
    }

    @Override
    public boolean isSupport(final HttpServletRequest request) {
        if (notFoundExecution != null) {
            return true;
        }
        return handlerExecutions.containsKey(getHandlerKey(request));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.getOrDefault(
                getHandlerKey(request),
                notFoundExecution
        );
    }

    private HandlerKey getHandlerKey(final HttpServletRequest request) {
        return new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
    }
}

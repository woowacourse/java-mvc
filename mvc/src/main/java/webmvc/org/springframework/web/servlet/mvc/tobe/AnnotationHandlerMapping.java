package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        initializeHandlerExecutions(new Reflections(basePackage));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerExecutions(final Reflections reflections) {
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            initializeByController(controllerClass);
        }
    }

    private void initializeByController(final Class<?> controllerClass) {
        try {
            final Object controller = controllerClass.getDeclaredConstructor().newInstance();
            final List<Method> requestMappingMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(this::isValidMethod)
                    .collect(Collectors.toList());

            for (final Method method : requestMappingMethods) {
                initializeByRequestMappingMethod(controller, method);
            }
        } catch (final Exception e) {
            // ignore
            log.info(e.getMessage());
        }
    }

    private void initializeByRequestMappingMethod(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();
        final String requestUri = requestMapping.value();

        for (final RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(
                    new HandlerKey(requestUri, requestMethod),
                    new HandlerExecution(method, controller)
            );
        }
    }

    private boolean isValidMethod(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class) &&
                method.getReturnType().equals(ModelAndView.class) &&
                new HashSet<>(List.of(method.getParameterTypes())).containsAll(List.of(HttpServletRequest.class, HttpServletResponse.class));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String method = request.getMethod();
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

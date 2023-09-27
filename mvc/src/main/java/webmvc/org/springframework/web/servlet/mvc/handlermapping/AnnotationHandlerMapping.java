package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (final Object targetPackage : basePackage) {
            addAllHandlerExecutionsInBasePackage(targetPackage);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addAllHandlerExecutionsInBasePackage(final Object base) {
        final Reflections reflections = new Reflections(base);
        final Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> clazz : controllerClass) {
            addHandlerExecutionsInController(clazz);
        }
    }

    private void addHandlerExecutionsInController(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        final List<Method> methodsAbleToHandleRequest = findMethodsAbleToHandleRequest(methods);

        for (final Method method : methodsAbleToHandleRequest) {
            saveAllPossibleHandlerExecutions(method);
        }
    }

    private List<Method> findMethodsAbleToHandleRequest(final Method[] methods) {
        return Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void saveAllPossibleHandlerExecutions(final Method method) {
        final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMappingAnnotation.method();
        final String path = requestMappingAnnotation.value();
        final HandlerExecution handlerExecution = HandlerExecution.from(method);

        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = retreiveHandlerKey(request);

        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey retreiveHandlerKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return new HandlerKey(requestURI, requestMethod);
    }
}

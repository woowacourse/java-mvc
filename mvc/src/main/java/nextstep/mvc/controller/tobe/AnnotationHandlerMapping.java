package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.NoConstructorException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> handlerClasses = getHandlerClasses();

        for (Class<?> handlerClass : handlerClasses) {
            initializeHandlerExecutions(handlerClass);
        }
    }

    private Set<Class<?>> getHandlerClasses() {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void initializeHandlerExecutions(Class<?> controllerClass) {
        final Object controller = newInstanceOf(controllerClass);
        final List<Method> handlerMethods = extractValidHandler(controllerClass);

        for (Method handlerMethod : handlerMethods) {
            final HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);

            putToHandlerExecutions(handlerMethod, handlerExecution);
        }
    }

    private Object newInstanceOf(Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = controllerClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new NoConstructorException();
        }
    }

    private List<Method> extractValidHandler(Class<?> controllerClass) {
        final Method[] declaredMethods = controllerClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(this::isValidRequestMapping)
                .collect(Collectors.toList());
    }

    private boolean isValidRequestMapping(Method controllerMethod) {
        final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return false;
        }

        if (requestMapping.value().isEmpty()) {
            return false;
        }

        return requestMapping.method().length > 0;
    }

    private void putToHandlerExecutions(Method controllerMethod, HandlerExecution handlerExecution) {
        final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);

        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
            logPathAndHandler(handlerKey, handlerExecution);
        }
    }

    private void logPathAndHandler(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        log.info("Path : " + handlerKey.getRequestMethod() + " " + handlerKey.getUrl() +
                ", Controller : " + handlerExecution.getController().getClass());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.from(request));
    }
}

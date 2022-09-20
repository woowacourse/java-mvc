package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toList;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.exception.NotSupportHandler;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        initHandlerExecution(basePackage);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private void initHandlerExecution(final Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = getAllControllers(reflections);

        for (Class<?> controllerClass : controllers) {
            final Method[] methods = controllerClass.getDeclaredMethods();
            putSupportedMethodInHandlerExecution(methods);
        }
    }

    private static Set<Class<?>> getAllControllers(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void putSupportedMethodInHandlerExecution(final Method[] methods) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final Object handler = getHandler(method);
            putHandlerExecution(method, requestMapping, handler);
        }
    }

    private static Object getHandler(final Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new NotSupportHandler();
        }
    }

    private void putHandlerExecution(final Method method, final RequestMapping requestMapping, final Object handler) {
        final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping);
        for (HandlerKey handlerKey : handlerKeys) {
            log.info("HandlerKey : {}", handlerKey);
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    private static List<HandlerKey> getHandlerKeys(final RequestMapping requestMapping) {
        if (requestMapping != null) {
            return Arrays.stream(requestMapping.method())
                    .map(method -> new HandlerKey(requestMapping.value(), method))
                    .collect(toList());
        }
        return new ArrayList<>();
    }
}

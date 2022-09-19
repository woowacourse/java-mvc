package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
    private static final int METHOD_INDEX = 0;

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
            putHandlerExecution(method, requestMapping);
        }
    }

    private void putHandlerExecution(final Method method, final RequestMapping requestMapping) {
        if (requestMapping != null) {
            final HandlerKey handlerKey = getHandlerKey(requestMapping);
            final Object handler = getHandler(method);
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    private static HandlerKey getHandlerKey(final RequestMapping requestMapping) {
        return new HandlerKey(requestMapping.value(),
                RequestMethod.valueOf(requestMapping.method()[METHOD_INDEX].name()));
    }

    private static Object getHandler(final Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new NotSupportHandler();
        }
    }
}

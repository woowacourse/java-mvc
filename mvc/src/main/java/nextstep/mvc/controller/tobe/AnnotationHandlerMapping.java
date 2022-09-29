package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);

        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::mappingHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, method);

        return handlerExecutions.get(handlerKey);
    }

    private void mappingHandlerExecutions(final Class<?> aClass, final Object instance) {
        Arrays.stream(aClass.getDeclaredMethods())
                .forEach(method -> mappingHandlerExecution(instance, method));
    }

    private void mappingHandlerExecution(final Object instance, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        for (final RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey key = new HandlerKey(url, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(instance, method));
        }
    }
}

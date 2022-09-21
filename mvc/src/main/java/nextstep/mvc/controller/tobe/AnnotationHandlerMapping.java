package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
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
        Reflections reflections = new Reflections(basePackage);

        final var classes = new HashSet<Class<?>>(reflections.getTypesAnnotatedWith(Controller.class));

        for (Class<?> clazz : classes) {
            initialize(clazz);
        }
    }

    private void initialize(final Class<?> clazz) {
        try {
            Object controller = clazz.getConstructor().newInstance();
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> makeHandlerExecutions(controller, method));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void makeHandlerExecutions(final Object controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> keys = getHandlerKeys(annotation.value(), annotation.method());
        HandlerExecution value = new HandlerExecution(method, controller);

        for (HandlerKey key : keys) {
            handlerExecutions.put(key, value);
        }
    }

    private List<HandlerKey> getHandlerKeys(final String requestUrl, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestUrl, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey key = new HandlerKey(uri, method);

        return handlerExecutions.get(key);
    }
}

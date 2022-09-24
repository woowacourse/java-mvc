package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int FIRST_INDEX = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final var reflections = new Reflections(basePackage);
        final var controllerScanner = new ControllerScanner(reflections);

        final var methods = getMethods(controllerScanner.getControllers());
        addHandlerExecutions(methods);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Map<Object, Method[]> getMethods(final Map<Class<?>, Object> controllers) {
        return controllers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> entry.getKey().getDeclaredMethods()));
    }

    private void addHandlerExecutions(final Map<Object, Method[]> methods) {
        for (Entry<Object, Method[]> controllerInstanceAndMethods : methods.entrySet()) {
            for (Method method : controllerInstanceAndMethods.getValue()) {
                final var requestMapping = method.getAnnotation(RequestMapping.class);
                final var handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[FIRST_INDEX]);
                handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstanceAndMethods.getKey(), method));
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var uri = request.getRequestURI();
        final var method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }
}

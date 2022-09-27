package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = ControllerScanner.from(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> clazz : controllers.keySet()) {
            addHandlerExecutions(clazz, controllers.get(clazz));
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach((key, value) -> log.info("{}, {}", key, value));
    }

    private void addHandlerExecutions(final Class<?> clazz, final Object controller) {
        List<Method> handlerMethods = getHandlerMethods(clazz);

        for (Method handlerMethod : handlerMethods) {
            List<HandlerKey> handlerKeys = mapHandlerKeys(handlerMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private List<Method> getHandlerMethods(final Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        return Stream.of(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private List<HandlerKey> mapHandlerKeys(final Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String requestURI = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        return Stream.of(requestMethods)
                .map(requestMethod -> new HandlerKey(requestURI, requestMethod))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.from(request);
        log.debug("Handler called. {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}

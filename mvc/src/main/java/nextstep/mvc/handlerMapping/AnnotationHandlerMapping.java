package nextstep.mvc.handlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.scan();

        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(
                controller,
                ReflectionUtils.withAnnotation(RequestMapping.class)
            );

            for (Method method : methods) {
                addToHandlerExecutions(controllers.get(controller), method);
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void addToHandlerExecutions(final Object controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        List<HandlerKey> keys = Arrays.stream(annotation.method())
            .map(it -> new HandlerKey(annotation.value(), it))
            .collect(Collectors.toUnmodifiableList());

        HandlerExecution execution = new HandlerExecution(controller, method);

        for (HandlerKey key : keys) {
            handlerExecutions.put(key, execution);
        }
    }

}

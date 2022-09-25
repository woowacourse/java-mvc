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
import nextstep.mvc.HandlerExecution;
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
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = getRequestMappingMethods(controller);

            for (Method method : methods) {
                addHandlerExecutions(controllers.get(controller), method);
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        return ReflectionUtils.getAllMethods(
            controller,
            ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        List<HandlerKey> keys = mapHandlerKeys(annotation.value(), annotation.method());
        HandlerExecution execution = new HandlerExecution(controller, method);

        for (HandlerKey key : keys) {
            handlerExecutions.put(key, execution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String uri, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(uri, requestMethod))
            .collect(Collectors.toUnmodifiableList());
    }

}

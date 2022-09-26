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
import nextstep.mvc.controller.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getAnnotatedControllers();

        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(controller.getKey(),
                ReflectionUtils.withAnnotation(RequestMapping.class));

            for (Method method : methods) {
                putHandlerExecution(controller.getValue(), method);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }

    private void putHandlerExecution(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final List<HandlerKey> handlerKeys = getHandlerKeys(requestMapping);
        final HandlerExecution handlerExecution = new HandlerExecution(instance, method);

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> getHandlerKeys(RequestMapping requestMapping) {
        return Arrays.stream(requestMapping.method())
            .map(it -> new HandlerKey(requestMapping.value(), it))
            .collect(Collectors.toUnmodifiableList());
    }
}

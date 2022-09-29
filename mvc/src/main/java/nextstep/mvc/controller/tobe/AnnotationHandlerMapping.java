package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Set<Method> methods = getRequestMappingMethods(entry.getKey());
            addHandlerExecutions(entry.getValue(), methods);
        }
    }

    private Set<Method> getRequestMappingMethods(final Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecutions(final Object controller, final Set<Method> methods) {
        for (Method method : methods) {
            addHandlerExecution(controller, method);
        }
    }

    private void addHandlerExecution(final Object controller,
                                     final Method method) {
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

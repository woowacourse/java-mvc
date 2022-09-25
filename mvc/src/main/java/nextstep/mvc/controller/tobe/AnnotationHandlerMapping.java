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
import org.reflections.util.ReflectionUtilsPredicates;
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
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Set<Method> requestMappingMethods = getRequestMappingMethods(entry.getKey());
            addHandlerExecutions(entry.getValue(), requestMappingMethods);
        }
    }

    private Set<Method> getRequestMappingMethods(final Class<?> handler) {
        return ReflectionUtils.getAllMethods(handler, ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
    }

    private void addHandlerExecutions(final Object instance, final Set<Method> requestMappingMethods) {
        for (Method requestMappingMethod : requestMappingMethods) {
            RequestMapping requestMapping = requestMappingMethod.getAnnotation(RequestMapping.class);
            addHandlerExecution(instance, requestMappingMethod, requestMapping);
        }
    }

    private void addHandlerExecution(final Object instance, final Method method, final RequestMapping requestMapping) {
        HandlerMethod handlerMethod = new HandlerMethod(instance, method);
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(handlerMethod));
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String path, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.getOrDefault(handlerKey, null);
    }
}

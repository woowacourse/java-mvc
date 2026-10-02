package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final ControllerScanner controllerScanner = ControllerScanner.from(basePackage);
        final Map<Class<?>, Object> controllerInstanceMap = controllerScanner.getControllers();

        getRequestMappingMethods(controllerInstanceMap.keySet()).forEach(method ->
                addHandlerExecutions(controllerInstanceMap, method, method.getAnnotation(RequestMapping.class)));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllerInstanceMap,
        final Method method, final RequestMapping requestMapping) {
        final Class<?> controller = method.getDeclaringClass();
        final HandlerExecution handlerExecution =
            new HandlerExecution(controllerInstanceMap.get(controller), method);

        mapHandlerKeys(requestMapping.value(), requestMapping.method())
            .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllers) {
        return controllers.stream()
            .map(Class::getMethods)
            .flatMap(Arrays::stream)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toSet());
    }

    private List<HandlerKey> mapHandlerKeys(final String url,
        final RequestMethod[] availableRequestMethods) {
        if (availableRequestMethods.length == 0 && RequestMethod.values().length != 0) {
            return mapHandlerKeys(url, RequestMethod.values());
        }

        return Arrays.stream(availableRequestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey =
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

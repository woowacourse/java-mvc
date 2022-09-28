package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
        for (Method method : requestMappingMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> clazz : classes) {
            mapRequestMappingMethods(methods, clazz);
        }
        return methods;
    }

    private void mapRequestMappingMethods(final Set<Method> methods, final Class<?> clazz) {
        for (Method method : clazz.getMethods()) {
            addMethod(methods, method);
        }
    }

    private void addMethod(final Set<Method> methods, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        methods.add(method);
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                      final RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            Class<?> clazz = method.getDeclaringClass();
            handlerExecutions.put(handlerKey, new HandlerExecution(controllers.get(clazz), method));
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String value, final RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(value, requestMethod));
        }
        return handlerKeys;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

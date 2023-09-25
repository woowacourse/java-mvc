package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Class<?>> controllerClasses = controllers.keySet();
        Set<Method> methods = getRequestMappingMethods(controllerClasses);
        methods.forEach(method -> {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        });
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllerMapper, final Method method, final RequestMapping requestMapping) {
        final String requestURI = requestMapping.value();
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestURI, requestMapping.method());
        handlerKeys.forEach(handlerKey -> {
            final Object instance = controllerMapper.get(method.getDeclaringClass());
            handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
        });
    }

    private List<HandlerKey> mapHandlerKeys(final String requestURI, final RequestMethod[] methods) {
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(requestURI, method))
                .collect(Collectors.toList());
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(controllerClass -> Arrays.stream(controllerClass.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }


    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}

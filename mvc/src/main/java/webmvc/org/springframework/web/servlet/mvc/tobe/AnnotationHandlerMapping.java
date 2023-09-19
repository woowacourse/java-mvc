package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
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
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        initializeHandlerMappings(controllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerMappings(Map<Class<?>, Object> controllers) {
        controllers.forEach((clazz, instance) -> {
            Set<Method> methods = getRequestMappingMethods(clazz);
            methods.forEach(method -> addHandlerMappings(instance, method));
        });
    }

    private Set<Method> getRequestMappingMethods(Class<?> aClass) {
        return ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addHandlerMappings(Object instance, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        handlerKeys.forEach(handlerKey -> addHandlerExecution(handlerKey, instance, method));
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    private void addHandlerExecution(HandlerKey handlerKey, Object instance, Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(instance, method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase()));
        return handlerExecutions.get(handlerKey);
    }
}

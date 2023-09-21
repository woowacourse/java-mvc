package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner scanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Method method : getRequestMappingMethods(controllers.keySet())) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        Set<Method> requestMappingMethods = new HashSet<>();
        for (Class<?> clazz : classes) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                    ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
            requestMappingMethods.addAll(methods);
        }
        return requestMappingMethods;
    }

    private void addHandlerExecutions(Map<Class<?>, Object> clazz, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            Object instance = clazz.get(method.getDeclaringClass());
            HandlerExecution execution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String mappingName, RequestMethod[] methods) {
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(mappingName, method))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

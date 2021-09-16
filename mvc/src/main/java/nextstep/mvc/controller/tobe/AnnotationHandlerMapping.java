package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Start Initializing AnnotationHandlerMapping!");

        ControllerScanner controllerScanner = new ControllerScanner();

        Map<Class<?>, Object> controllers = controllerScanner.getControllers(basePackage);
        Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

        for (Method method : requestMappingMethods) {
            addHandlerExecution(controllers, method);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();

        for (Class<?> controller : controllers) {
            methods.addAll(
                    ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class))
            );
        }

        return methods;
    }

    private void addHandlerExecution(Map<Class<?>, Object> maps, Method method) {
        Class<?> clazz = method.getDeclaringClass();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());

        for (HandlerKey key : handlerKeys) {
            handlerExecutions.put(key, new HandlerExecution(maps.get(clazz), method));
            log.info("Path : {}, Controller : {}", key, clazz);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String requestUrl, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(requestUrl, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

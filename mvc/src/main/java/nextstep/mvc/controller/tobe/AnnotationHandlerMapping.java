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
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllerClassWithInstance = scanner.getControllers();
        registerHandlerExecutions(controllerClassWithInstance);
    }

    private void registerHandlerExecutions(Map<Class<?>, Object> controllerClassWithInstance) {
        for (Entry<Class<?>, Object> entry : controllerClassWithInstance.entrySet()) {
            Object instance = entry.getValue();
            Class<?> controllerClass = entry.getKey();
            Set<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);
            addHandlerExecution(instance, requestMappingMethods);
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecution(Object instance, Set<Method> requestMappingMethods) {
        for (Method method : requestMappingMethods) {
            List<HandlerKey> handlerKeys = makeHandlerKey(method);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.putAll(makeHandlerExecutions(handlerKeys, handlerExecution));
        }
    }

    private Map<HandlerKey, HandlerExecution> makeHandlerExecutions(List<HandlerKey> handlerKeys,
                                                                    HandlerExecution handlerExecution) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
        return handlerExecutions;
    }

    private List<HandlerKey> makeHandlerKey(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] httpMethods = requestMapping.method();
        return Arrays.stream(httpMethods)
                .map(httpMethod -> new HandlerKey(uri, httpMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}

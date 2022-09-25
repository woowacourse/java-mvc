package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
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
            Class<?> aClass = entry.getKey();
            Set<Method> requestMappingMethods = getRequestMappingMethods(aClass);
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
            HandlerKey handlerKey = makeHandlerKey(method);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerKey makeHandlerKey(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod httpMethod = requestMapping.method()[0];
        return new HandlerKey(uri, httpMethod);
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}

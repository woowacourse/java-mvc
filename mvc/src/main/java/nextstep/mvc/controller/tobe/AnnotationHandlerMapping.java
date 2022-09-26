package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.support.ControllerScanner;
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

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methods = getRequestMappingMethods(controller);
            addHandlerExecutions(methods, controllers.get(controller));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void addHandlerExecutions(Set<Method> methods, Object instance) {
        for (Method method : methods) {
            setUpHandlerExecutions(method, instance);
        }
    }

    private void setUpHandlerExecutions(Method method, Object instance) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution value = new HandlerExecution(method, instance);
            handlerExecutions.put(key, value);
            log.info("Path : {} , Method : {}", requestMapping.value(), requestMethod);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

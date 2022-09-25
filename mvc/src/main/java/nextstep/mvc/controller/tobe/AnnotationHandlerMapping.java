package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            handlerExecutions.putAll(initExecutions());
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            log.error("error to initialize AnnotationHandlerMapping");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions
                .get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private Map<HandlerKey, HandlerExecution> initExecutions() throws Exception {
        ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> controller : controllers.keySet()) {
            addRequestMappingMethod(handlerExecutions, controller, controllers.get(controller));
        }
        return handlerExecutions;
    }

    private void addRequestMappingMethod(final Map<HandlerKey, HandlerExecution> handlerExecutions,
                                         final Class<?> controller, final Object instance) {
        Set<Method> allMethods = getRequestMappingMethods(controller);
        for (Method method : allMethods) {
            handlerExecutions.putAll(createMethodHandlers(instance, method));
        }
    }

    @SuppressWarnings("unchecked")
    private Set<Method> getRequestMappingMethods(final Class<?> controller) {
        return ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private Map<HandlerKey, HandlerExecution> createMethodHandlers(final Object instance, final Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = toHandlerKey(requestMapping, requestMethod);
            HandlerExecution handlerExecution = toHandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
        return handlerExecutions;
    }

    private HandlerKey toHandlerKey(final RequestMapping requestMapping, final RequestMethod requestMethod) {
        return new HandlerKey(requestMapping.value(), requestMethod);
    }

    private HandlerExecution toHandlerExecution(final Object instance, final Method method) {
        return new HandlerExecution(instance, method);
    }
}

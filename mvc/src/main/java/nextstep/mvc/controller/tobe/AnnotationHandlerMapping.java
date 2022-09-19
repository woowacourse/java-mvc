package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
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
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Object basePackage : basePackages) {
            Set<Class<?>> classes = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
            handlerExecutions.putAll(createClassExecutions(classes));
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createClassExecutions(final Set<Class<?>> classes) throws Exception {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> controller : classes) {
            handlerExecutions.putAll(createControllerExecutions(controller));
        }
        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createControllerExecutions(final Class<?> controller) throws Exception {
        var instance = controller.getDeclaredConstructor()
                .newInstance();
        Method[] methods = controller.getDeclaredMethods();
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Method method : methods) {
            handlerExecutions.putAll(createMethodHandlers(instance, method));
        }
        return handlerExecutions;
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

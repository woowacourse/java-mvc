package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        log.info("Initialized AnnotationHandlerMapping!");
        final Map<Class<?>, Object> controllers = getMappedControllers(basePackage);
        addHandlerExecution(controllers);
    }

    public Map<Class<?>, Object> getMappedControllers(final Object... targetPackages) {
        final Reflections reflections = new Reflections(targetPackages);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        return controllerScanner.getControllers();
    }


    private void addHandlerExecution(final Map<Class<?>, Object> controllers) {
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(controller.getKey(),
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            generateHandlerExecutions(requestMappingMethods, controller);
        }
    }

    private void generateHandlerExecutions(final Set<Method> requestMappedMethods,
                                           final Entry<Class<?>, Object> controller) {
        for (Method method : requestMappedMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(requestMapping, method, controller.getValue());
        }
    }

    private void addHandlerExecution(final RequestMapping requestMapping, final Method method, final Object handler) {
        final String value = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method, handler);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        log.debug("AnnotationHandlerMapping Method: {}, RequestURI: {}", request.getMethod(), request.getRequestURI());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

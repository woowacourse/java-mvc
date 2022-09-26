package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }
    
    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            final List<Method> methodsWithRequestMapping = findMethodsWithRequestMappingAnnotation(clazz);
            addHandlerKeys(methodsWithRequestMapping, controllers.get(clazz));
        }
    }

    private List<Method> findMethodsWithRequestMappingAnnotation(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableList());
    }

    private void addHandlerKeys(final List<Method> methodsWithRequestMapping, final Object controller) {
        for (Method method : methodsWithRequestMapping) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String value = requestMapping.value();
            final RequestMethod[] methods = requestMapping.method();
            addHandlerKey(method, value, methods, controller);
        }
    }

    private void addHandlerKey(final Method method, final String value, final RequestMethod[] methods,
                               final Object controller) {
        for (RequestMethod requestMethod : methods) {
            handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

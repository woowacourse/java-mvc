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
        for (Object packageName : basePackage) {
            final ControllerScanner controllerScanner = new ControllerScanner((String) packageName);
            final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            initializeAnnotationHandlerMapping(controllers);
        }
    }

    private void initializeAnnotationHandlerMapping(final Map<Class<?>, Object> classesWithController) {
        for (Class<?> clazz : classesWithController.keySet()) {
            final List<Method> methodsWithRequestMapping = extractMethodsWithRequestMapping(clazz);
            for (Method method : methodsWithRequestMapping) {
                initializeHandlerExecutions(classesWithController.get(clazz), method);
            }
        }
    }

    private List<Method> extractMethodsWithRequestMapping(final Class<?> clazz) {
        final Method[] methods = clazz.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableList());
    }


    private void initializeHandlerExecutions(final Object handler, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String value = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.of(method));
        return handlerExecutions.get(handlerKey);
    }
}

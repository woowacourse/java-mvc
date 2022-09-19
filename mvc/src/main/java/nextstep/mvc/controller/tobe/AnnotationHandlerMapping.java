package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        mapHandlerExecution(controllers);
    }

    private void mapHandlerExecution(final Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            final List<Method> methods = getMethodsAnnotatedWithRequestMapping(controller);
            putHandlerExecution(methods, controller);
        }
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(final Class<?> controller) {
        final Method[] declaredMethods = controller.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerExecution(final List<Method> methods, final Class<?> controller) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        log.debug("Request Mapping uri : {}, method : {}", url, requestMethod);

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}

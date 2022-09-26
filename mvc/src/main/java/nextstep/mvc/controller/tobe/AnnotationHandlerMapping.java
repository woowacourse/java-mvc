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
import nextstep.web.support.RequestUrl;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HandlerExecutions(new HashMap<>());
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            putControllerToHandlerExecutions(controller.getKey(), controller.getValue());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestUrl requestURI = new RequestUrl(request.getRequestURI());
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        return handlerExecutions.get(requestURI, requestMethod);
    }

    private void putControllerToHandlerExecutions(final Class<?> controller, final Object instance) {
        Set<Method> controllerMethods = ReflectionUtils.getAllMethods(controller,
                ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
        for (Method method : controllerMethods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            putHandlerKeyAndExecution(instance, method, requestMapping);
        }
    }

    private void putHandlerKeyAndExecution(final Object instance, final Method method,
                                           final RequestMapping requestMapping) {
        RequestUrl requestUrl = new RequestUrl(requestMapping.value());
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(requestUrl, requestMethod, instance, method);
        }
    }
}

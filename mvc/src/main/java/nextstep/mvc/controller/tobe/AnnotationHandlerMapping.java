package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.ControllerScanner;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            final Map<Class<?>, Object> controllers = ControllerScanner.scan(basePackage);
            for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
                final Set<Method> methods = ReflectionUtils.getAllMethods(entry.getKey(),
                        ReflectionUtils.withAnnotation(RequestMapping.class));
                addHandlerExecutionWithMethod(entry, methods);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        handlerExecutions.keySet().forEach(handlerKey -> log.info(handlerKey.toString()));
    }

    private void addHandlerExecutionWithMethod(Map.Entry<Class<?>, Object> entry, Set<Method> methods) {
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final RequestMethod[] requestMethods = requestMapping.method();
            final String path = requestMapping.value();
            addHandlerExecution(entry.getValue(), method, requestMethods, path);
        }
    }

    private void addHandlerExecution(Object controller, Method method, RequestMethod[] requestMethods, String path) {
        for (RequestMethod requestMethod : requestMethods) {
            handlerExecutions.put(new HandlerKey(path, requestMethod),
                    new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}
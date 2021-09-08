package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
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
        try {
            for (Object base : basePackage) {
                Reflections reflections = new Reflections(base);
                final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
                for (Class<?> controllerClass : controllerClasses) {
                    final Method[] methods = controllerClass.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            final RequestMethod[] requestMethods = requestMapping.method();
                            final String path = requestMapping.value();
                            for (RequestMethod requestMethod : requestMethods) {
                                handlerExecutions.put(new HandlerKey(path, requestMethod),
                                        new HandlerExecution(controllerClass.getDeclaredConstructor().newInstance(), method));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        return handlerExecutions.keySet().stream()
                .filter(handlerKey -> handlerKey.isSame(requestURI, method))
                .map(handlerKey -> handlerExecutions.get(handlerKey))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var reflections = new Reflections(basePackage);
        final var controllers = reflections.getTypesAnnotatedWith(Controller.class);
        controllers.forEach(this::addControllerMethods);
    }

    private void addControllerMethods(Class<?> controller) {
        final var methods = controller.getMethods();
        for (Method method : methods) {
            addAnnotatedWithRequestMapping(controller, method);
        }
    }

    private void addAnnotatedWithRequestMapping(Class<?> controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final var annotation = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(controller, method, annotation);
        }
    }

    private void addHandlerExecution(Class<?> controller, Method method, RequestMapping requestMapping) {
        final var mappingUrl = requestMapping.value();
        final var mappingRequestMethods = requestMapping.method();
        for (RequestMethod requestMethod : mappingRequestMethods) {
            handlerExecutions.put(new HandlerKey(mappingUrl, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var key = new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod()));

        return handlerExecutions.get(key);
    }
}

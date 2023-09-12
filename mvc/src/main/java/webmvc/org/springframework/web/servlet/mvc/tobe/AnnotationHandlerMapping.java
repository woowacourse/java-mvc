package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Method[] methods = controller.getMethods();
            Arrays.stream(methods)
                  .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                  .forEach(this::putHandlers);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlers(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : annotation.method()) {
            final var handlerKey = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }
}

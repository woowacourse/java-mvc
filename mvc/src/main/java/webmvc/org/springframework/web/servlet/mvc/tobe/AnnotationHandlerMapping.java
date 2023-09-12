package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.reflections.util.ReflectionUtilsPredicates.withAnnotation;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
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

    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = ControllerScanner.from(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.controllers();
        for (Class<?> clazz : controllers.keySet()) {
            putExecutionBy(clazz, controllers.get(clazz));
        }
    }

    private void putExecutionBy(Class<?> clazz, Object controller) {
        Set<Method> methods = ReflectionUtils.getMethods(clazz, withAnnotation(RequestMapping.class));
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                    new HandlerKey(annotation.value(), annotation.method()),
                    new HandlerExecution(controller, method)
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(method)));
    }
}

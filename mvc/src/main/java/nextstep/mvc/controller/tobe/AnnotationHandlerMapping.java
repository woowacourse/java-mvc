package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            addAllHandlerExecutions(clazz, methods);
        }
    }

    private void addAllHandlerExecutions(Class<?> clazz, Set<Method> methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String url = annotation.value();
                RequestMethod requestMethod = annotation.method()[0];
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method));
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        return handlerExecutions.getOrDefault(new HandlerKey(url, requestMethod),
                handlerExecutions.get(new HandlerKey("/404", RequestMethod.GET)));
    }
}

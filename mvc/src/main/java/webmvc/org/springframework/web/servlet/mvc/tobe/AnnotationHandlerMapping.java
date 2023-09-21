package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ReflectionUtilsPredicates;
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
        ControllerScanner scanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Class<?> controller : controllers.keySet()) {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(controller, ReflectionUtilsPredicates.withAnnotation(RequestMapping.class));
            for (Method declaredMethod : allMethods) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    Object instance = getInstance(controller);
                    addExecution(instance, declaredMethod);
                }
            }
        }
    }

    private void addExecution(Object instance, Method method) {
        if (instance == null) {
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution execution = new HandlerExecution(instance, method);
            handlerExecutions.put(key, execution);
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.getOrDefault(handlerKey, null);
    }
}

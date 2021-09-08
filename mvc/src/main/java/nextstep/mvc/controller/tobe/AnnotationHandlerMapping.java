package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        if (basePackage.length != 0) {
            for (Object targetPackage : basePackage) {
                Reflections reflections = new Reflections(targetPackage);
                register(reflections);
            }
            return;
        }
        Reflections reflections = new Reflections();
        register(reflections);
    }

    private void register(Reflections reflections) {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            Method[] methods = clazz.getDeclaredMethods();
            checkMethodWithAnnotation(clazz, methods);
        }
        handlerExecutions.keySet().forEach(path ->
                log.info("Path : {}, Controller : {}", path, handlerExecutions.get(path).getClass()));
    }

    private void checkMethodWithAnnotation(Class<?> clazz, Method[] methods) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                setHandlerExecutions(clazz, method);
            }
        }
    }

    private void setHandlerExecutions(Class<?> clazz, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String value = annotation.value();
        RequestMethod[] method1 = annotation.method();
        for (RequestMethod requestMethod : method1) {
            try {
                HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                HandlerExecution handlerExecution = new HandlerExecution(method, clazz.getConstructor().newInstance());
                handlerExecutions.put(handlerKey, handlerExecution);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error(e.toString());
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String methodName = request.getMethod();
        RequestMethod requestMethod = RequestMethod.find(methodName);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

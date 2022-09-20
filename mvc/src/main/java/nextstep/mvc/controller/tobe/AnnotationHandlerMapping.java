package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        initializeHandlerExecutions();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void initializeHandlerExecutions() {
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        controllerScanner.initialize();

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.keySet().forEach(clazz -> {
            Method[] methods = clazz.getDeclaredMethods();
            addHandlerExecutions(clazz, methods);
        });
    }

    private void addHandlerExecutions(Class<?> clazz, Method[] methods) {
        for (Method method : methods) {
            registerHandlerKeyAndHandlerExecutionIfRequestMappingAnnotationExists(clazz, method);
        }
    }

    private void registerHandlerKeyAndHandlerExecutionIfRequestMappingAnnotationExists(Class<?> clazz, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String path = annotation.value();
        RequestMethod[] httpMethods = annotation.method();
        for (RequestMethod httpMethod : httpMethods) {
            try {
                handlerExecutions.put(new HandlerKey(path, httpMethod),
                        new HandlerExecution(clazz.getConstructor().newInstance(), method));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

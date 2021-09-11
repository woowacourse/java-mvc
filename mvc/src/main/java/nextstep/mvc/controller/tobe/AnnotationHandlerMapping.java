package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            fillHandlerExecutions(controllerClass);
        }
    }

    private void fillHandlerExecutions(Class<?> controllerClass) {
        List<Method> handlers = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
        for (Method handler : handlers) {
            fillHandlerExecutions(controllerClass, handler);
        }
    }

    private void fillHandlerExecutions(Class<?> controllerClass, Method handler) {
        RequestMapping annotation = handler.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : annotation.method()) {
            String annotationValue = annotation.value();
            Object instance = createInstance(controllerClass);
            HandlerKey handlerKey = new HandlerKey(annotationValue, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, instance);

            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Used {} class to map [{}] method as handler for {} {}",
                    controllerClass.getSimpleName(),
                    handler.getName(),
                    requestMethod,
                    annotationValue);
        }
    }

    private Object createInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException exception) {
            String controllerName = controllerClass.getSimpleName();
            log.error("exception while making controller instance of {}", controllerName);
            throw new ControllerCreationException(controllerName);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}

package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.exception.MvcException;
import nextstep.mvc.handler.annotation.HandlerExecution;
import nextstep.mvc.handler.annotation.HandlerKey;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            fillHandlerExecutions(controllerClass);
        }
    }

    private void fillHandlerExecutions(Class<?> controllerClass) {
        Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> fillHandlerExecutionsByMethods(controllerClass, method));
    }

    private void fillHandlerExecutionsByMethods(Class<?> controllerClass, Method handler) {
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
            throw new MvcException(
                    String.format("Error while creating instance of controller %s for handler mapping process", controllerClass.getSimpleName()),
                    exception
            );
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}

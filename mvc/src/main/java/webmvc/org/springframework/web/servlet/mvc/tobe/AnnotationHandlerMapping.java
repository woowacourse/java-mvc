package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Object instance = controller.getDeclaredConstructor().newInstance();
            Method[] methods = controller.getDeclaredMethods();

            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecutions(instance, method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Object target, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(target, method);

        RequestMethod[] requestMethods = annotation.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}

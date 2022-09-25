package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : controllers) {
            mappingMethod(clazz);
        }
    }

    private void mappingMethod(Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(m -> mappingHandlerExecution(clazz, m));
    }

    private void mappingHandlerExecution(Class<?> clazz, Method method) {
        try {
            final Object controller = clazz.getConstructor().newInstance();
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : annotation.method()) {
                handlerExecutions.put(
                        new HandlerKey(annotation.value(), requestMethod),
                        new HandlerExecution(controller, method)
                );
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }
}

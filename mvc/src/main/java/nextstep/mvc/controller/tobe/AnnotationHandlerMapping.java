package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(this.basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            final Method[] methods = controller.getMethods();
            for (Method method : methods) {
                hasRequestMappingAnnotation(controller, method);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void hasRequestMappingAnnotation(final Class<?> controller, final Method method) {
        if (method.getAnnotation(RequestMapping.class) != null) {
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            final RequestMethod[] requestMethods = annotation.method();
            initHandlerExecutions(controller, method, annotation, requestMethods);
        }
    }

    private void initHandlerExecutions(final Class<?> controller, final Method method, final RequestMapping annotation,
                           final RequestMethod[] requestMethods) {
        try {
            for (RequestMethod requestMethod : requestMethods) {
                handlerExecutions.put(new HandlerKey(annotation.value(), requestMethod),
                        new HandlerExecution(controller.getDeclaredConstructor().newInstance(), method));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.error(e.getMessage());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.find(method)));
    }
}

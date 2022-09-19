package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.exception.FailMapHandler;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.get(TypesAnnotated.with(Controller.class).asClass());
        for (Class<?> aClass : classes) {
            mapRequestToMethod(aClass);
        }
    }

    private void mapRequestToMethod(final Class<?> aClass) {
        final Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            putHandlerExecutions(method, requestMapping);
        }
    }

    private void putHandlerExecutions(final Method method, final RequestMapping requestMapping) {
        if (requestMapping != null) {
            final String url = requestMapping.value();
            final RequestMethod requestMethod = requestMapping.method()[0];
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            final Object handler = mapHandler(method);

            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    private Object mapHandler(final Method method) {
        final Object handler;
        try {
            handler = method.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new FailMapHandler();
        }
        return handler;
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

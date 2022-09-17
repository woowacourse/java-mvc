package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object packageName : basePackage) {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            mapHandlers(controllerClasses);
        }
    }

    private void mapHandlers(final Set<Class<?>> controllerClasses) {
        for (Class<?> clazz : controllerClasses) {
            Method[] methods = clazz.getMethods();
            mapMethods(clazz, methods);
        }
    }

    private void mapMethods(final Class<?> clazz, final Method[] methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            mapAnnotatedMethod(clazz, method, requestMapping);
        }
    }

    private void mapAnnotatedMethod(final Class<?> clazz, final Method method, final RequestMapping requestMapping) {
        if (requestMapping == null) {
            return;
        }
        String url = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            mapRequestMethod(clazz, method, url, requestMethod);
        }
    }

    private void mapRequestMethod(final Class<?> clazz, final Method method, final String url,
                           final RequestMethod requestMethod) {
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            handlerExecutions.put(handlerKey, new HandlerExecution(constructor.newInstance(), method));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

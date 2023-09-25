package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        final Set<Class<?>> controllers = getControllers();
        registerControllers(controllers);
    }

    private Set<Class<?>> getControllers() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerControllers(final Set<Class<?>> controllers) {
        
        for (Class<?> controller : controllers) {
            final Method[] methods = controller.getMethods();
            for (Method method : methods) {
                registerHandler(controller, method);
            }
        }
    }

    private void registerHandler(final Class<?> controller, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final Controller controllerAnnotation = controller.getDeclaredAnnotation(Controller.class);
        final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

        final String url = controllerAnnotation.path() + requestMappingAnnotation.value();
        for (RequestMethod requestMethod : requestMappingAnnotation.method()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    @Override
    public boolean isSupport(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String requestMethod = request.getMethod();
        return handlerExecutions.containsKey(new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod)));
    }
}

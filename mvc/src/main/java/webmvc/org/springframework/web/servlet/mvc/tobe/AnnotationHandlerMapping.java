package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        Set<Class<?>> controllers = getControllers();
        registerControllers(controllers);
    }

    private Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerControllers(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                registerHandler(controller, method);
            }
        }
    }

    private void registerHandler(Class<?> controller, Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        Controller controllerAnnotation = controller.getDeclaredAnnotation(Controller.class);
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

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
    public Object executeHandler(HttpServletRequest request, HttpServletResponse response) {
        final HandlerExecution handlerExecution = (HandlerExecution) getHandler(request);
        return handlerExecution.handle(request, response);
    }

    @Override
    public boolean isSupport(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String requestMethod = request.getMethod();
        return handlerExecutions.containsKey(new HandlerKey(requestURI, RequestMethod.valueOf(requestMethod)));
    }
}

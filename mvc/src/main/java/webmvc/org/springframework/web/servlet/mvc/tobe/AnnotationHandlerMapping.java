package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object pacakage : basePackage) {
            Set<Class<?>> controllers = getControllers((String) pacakage);
            registerControllers(controllers);
        }
    }

    private Set<Class<?>> getControllers(String pacakge) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage(pacakge));
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

        String url = controllerAnnotation.path() + requestMappingAnnotation.value();
        for (RequestMethod requestMethod : requestMappingAnnotation.method()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}

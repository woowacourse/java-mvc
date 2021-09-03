package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        mapControllers(controllerClasses);
        LOG.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapControllers(Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            Method[] methods = controllerClass.getDeclaredMethods();
            mapMethods(controllerClass, methods);
        }
    }

    private void mapMethods(Class<?> controllerClass, Method[] methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            mapRequestMapping(controllerClass, method, requestMapping);
        }
    }

    private void mapRequestMapping(Class<?> controllerClass, Method method, RequestMapping requestMapping) {
        if (Objects.nonNull(requestMapping)) {
            String path = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            mapRequestMethods(controllerClass, method, path, requestMethods);
        }
    }

    private void mapRequestMethods(Class<?> controllerClass, Method method, String path,
                                   RequestMethod[] requestMethods) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            HandlerExecution handlerExecution;
            handlerExecution = new HandlerExecution(controllerClass, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

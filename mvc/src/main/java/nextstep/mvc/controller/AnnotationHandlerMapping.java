package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        controllerScanner = new ControllerScanner(basePackage);
        handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        controllerScanner.initialize();
        final List<Object> controllers = controllerScanner.getControllers();
        putHandlerExecutionsOfHandlers(controllers);
        LOG.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutionsOfHandlers(List<Object> handlers) {
        for (Object handler : handlers) {
            final Class<?> handlerClass = handler.getClass();
            LOG.info("Annotation Controller 등록 : {}", handlerClass.getName());
            final Set<Method> methods = ReflectionUtils.getAllMethods(handlerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            putHandlerExecutionsOfHandlerMethods(handler, methods);
        }
    }

    private void putHandlerExecutionsOfHandlerMethods(Object handler, Set<Method> methods) {
        for (Method method : methods) {
            LOG.info("Annotation Controller {} 의 method {} 등록", handler.getClass().getName(), method.getName());
            final RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);

            final String requestUri = requestMappingAnnotation.value();
            final RequestMethod requestMethod = requestMappingAnnotation.method();

            final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();

        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

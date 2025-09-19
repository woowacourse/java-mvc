package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach(this::registerControllerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String path = request.getRequestURI();
        if (request.getContextPath() != null) {
            path = path.substring(request.getContextPath().length());
        }

        final HandlerKey requestHandlerKey = new HandlerKey(path, RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(requestHandlerKey);
    }

    private void registerControllerHandlers(Class<?> controllerClass, Object controller) {
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerExecution(method, controller));
    }

    private void registerHandlerExecution(Method handlerMethod, Object controller) {
        final RequestMapping requestMapping = handlerMethod.getDeclaredAnnotation(RequestMapping.class);
        final String path = requestMapping.value();

        Arrays.stream(requestMapping.method())
                .forEach(httpMethod -> registerSingleHandler(path, httpMethod, controller, handlerMethod));
    }

    private void registerSingleHandler(String path, RequestMethod httpMethod, Object controller, Method handlerMethod) {
        final HandlerKey handlerKey = new HandlerKey(path, httpMethod);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("해당 경로에 대한 요청에 대해 중복되는 핸들러가 존재합니다: " + handlerKey);
        }

        handlerExecutions.put(handlerKey, handlerExecution);
    }
}

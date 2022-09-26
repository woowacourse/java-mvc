package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        List<Object> values = Collections.singletonList(controllers.values());
        for (Object controller : values) {
            addHandlerExecutions(controller);
        }
        logInitializedRequestPath();
    }

    private void addHandlerExecutions(final Object controller) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                addHandlerExecutions(controller, method, requestMapping);
            }
        }
    }

    private void addHandlerExecutions(final Object controller,
                                      final Method method,
                                      final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            handlerExecutions.add(handlerKey, handlerExecution);
        }
    }

    private void logInitializedRequestPath() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.getHandlers()
                .forEach(handlerKey -> log.info("Path : {}", handlerKey));
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.getHandlerExecution(request.getRequestURI(), request.getMethod());
    }
}

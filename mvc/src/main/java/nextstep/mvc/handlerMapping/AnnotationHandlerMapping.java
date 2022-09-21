package nextstep.mvc.handlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> annotatedControllers = controllerScanner.getAnnotatedControllers();

        for (Class<?> controller : annotatedControllers) {
            Set<Method> annotatedMethods = ReflectionUtils.getAllMethods(controller,
                ReflectionUtils.withAnnotation(RequestMapping.class));
            try {
                setHandlerExecutions(controller, annotatedMethods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }

    private void setHandlerExecutions(final Class<?> handler, final Set<Method> annotatedMethods) throws Exception {
        final Object instance = handler.getDeclaredConstructor().newInstance();
        for (Method method : annotatedMethods) {
            putHandlerExecution(instance, method);
        }
    }

    private void putHandlerExecution(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();

        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}

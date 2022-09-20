package nextstep.mvc.handlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> annotatedController = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> handler : annotatedController) {
            Set<Method> annotatedMethods = Arrays.stream(handler.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
            try {
                setHandlerExecutions(handler, annotatedMethods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(instance, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

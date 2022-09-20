package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.context.PeanutContainer;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final Reflections reflections;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
        this.reflections = new Reflections(basePackages);
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        final List<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toUnmodifiableList());

        for (final Class<?> handlerClass : handlers) {
            for (final Method handlerMethod : handlerClass.getDeclaredMethods()) {
                final RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    final String url = requestMapping.value();
                    final RequestMethod[] requestMethods = requestMapping.method();
                    for (final RequestMethod requestMethod : requestMethods) {
                        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                        final Object handler = PeanutContainer.INSTANCE.getPeanut(handlerClass);
                        final HandlerExecution handlerExecution = new HandlerExecution(handler, handlerMethod);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.context.PeanutContainer;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final List<Class<?>> handlers = getControllerAnnotationClasses();

        for (final Class<?> handlerClass : handlers) {
            for (final Method handlerMethod : handlerClass.getDeclaredMethods()) {
                final Object handler = PeanutContainer.INSTANCE.getPeanut(handlerClass);
                final RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    final String url = requestMapping.value();
                    for (final RequestMethod requestMethod : requestMapping.method()) {
                        handlerExecutions.put(
                                new HandlerKey(url, requestMethod),
                                new HandlerExecution(handler, handlerMethod));
                    }
                }
            }
        }
    }

    private List<Class<?>> getControllerAnnotationClasses() {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        scanController(new Reflections(basePackages));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanController(final Reflections reflections) {
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::scanMethod);
    }

    private void scanMethod(final Class<?> controller) {
        Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> addHandler(controller, method));
    }

    private void addHandler(final Class<?> clazz, final Method method) {
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        final String url = requestMapping.value();

        Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method)));
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

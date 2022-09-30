package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String DEFAULT_URL = "/";
    private static final String DEFAULT_FILE_PATH = "/index.jsp";

    private final Object[] basePackage;
    private final Map<HandlerKey, Handleable> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        initDefaultMapping();
        ControllerScanner.getInstance().scan(basePackage)
                .forEach(this::addAnnotatedMethodToHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initDefaultMapping() {
        handlerExecutions.put(
                new HandlerKey(DEFAULT_URL, RequestMethod.GET),
                (req, res) -> DEFAULT_FILE_PATH);
    }

    private void addAnnotatedMethodToHandlerExecutions(final Class<?> clazz, final Object instance) {
        findAnnotatedMethod(clazz)
                .forEach(method -> addToHandlerExecutions(instance, method));
    }

    private List<Method> findAnnotatedMethod(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addToHandlerExecutions(Object instance, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey.createFrom(requestMapping)
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(method, instance)));
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);
        return handlerExecutions.get(key);
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    public static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            Object handler = createHandler(aClass);
            addHandlerExecutions(handler);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private static Object createHandler(final Class<?> aClass) {
        try {
            return aClass.getConstructor()
                    .newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException exception) {
            log.warn(exception.getMessage());
            throw new IllegalStateException();
        }
    }

    private void addHandlerExecutions(final Object handler) {
        Class<?> aClass = handler.getClass();
        List<Method> methods = Arrays.stream(aClass.getMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_CLASS))
                .collect(Collectors.toList());
        methods.forEach(method -> addHandlerExecution(handler, method));
    }

    private void addHandlerExecution(final Object handler, final Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_CLASS);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

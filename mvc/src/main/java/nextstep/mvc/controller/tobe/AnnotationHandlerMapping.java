package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        addHandlerExecutions(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private void addHandlerExecutions(final Set<Class<?>> handlers) {
        for (Class<?> handler : handlers) {
            initRequestMethods(handler);
        }
    }

    private void initRequestMethods(final Class<?> handler) {
        Object executableObject = createNewInstance(handler);
        Set<Method> methods = ReflectionUtils.getMethods(handler,
                method -> method.isAnnotationPresent(RequestMapping.class));

        for (Method method : methods) {
            registerHandlerExecution(executableObject, method);
        }
    }

    private Object createNewInstance(final Class<?> handler) {
        try {
            Constructor<?> constructor = handler.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            log.error("Initialization failed", e);
            throw new IllegalStateException();
        }
    }

    private void registerHandlerExecution(final Object executableObject, final Method method) {
        HandlerMethod handlerMethod = new HandlerMethod(executableObject, method);
        List<HandlerKey> handlerKeys = createHandlerKeys(method);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(handlerMethod));
        }
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        try {
            return handlerExecutions.get(handlerKey);
        } catch (NullPointerException e) {
            log.error("Not Found Exception", e);
            throw new IllegalStateException();
        }
    }
}

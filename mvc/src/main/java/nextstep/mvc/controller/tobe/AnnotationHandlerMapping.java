package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
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

    @Override
    public void initialize() {
        Set<Class<?>> classes = getControllers();

        for (Class<?> clazz : classes) {
            addHandlerExecutions(clazz);
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach((key, value) -> log.info("{}, {}", key, value));
    }

    private Set<Class<?>> getControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void addHandlerExecutions(final Class<?> clazz) {
        Object controller = instantiate(clazz);
        List<Method> handlerMethods = getHandlerMethods(clazz);

        for (Method handlerMethod : handlerMethods) {
            List<HandlerKey> handlerKeys = HandlerKey.from(handlerMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private Object instantiate(final Class<?> clazz) {
        try {
            return clazz.getConstructor()
                    .newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Controller instantiation failed.", e);
        }
    }

    private List<Method> getHandlerMethods(final Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        return Stream.of(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.from(request);
        log.debug("Handler called. {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}

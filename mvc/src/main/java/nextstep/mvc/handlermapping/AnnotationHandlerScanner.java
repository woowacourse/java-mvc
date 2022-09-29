package nextstep.mvc.handlermapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class AnnotationHandlerScanner {

    private final Reflections reflections;

    public AnnotationHandlerScanner(Object[] bassPackage) {
        reflections = new Reflections(bassPackage);
    }

    public Map<HandlerKey, HandlerExecution> scan() {
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Class<?> ctrl : controllers) {
            Object controller = createControllerInstance(ctrl);
            Set<Method> methods = getControllerMethods(controller);
            setHandlerExecutions(controller, methods, handlerExecutions);
        }
        return handlerExecutions;
    }

    private Object createControllerInstance(final Class<?> handlerClass) {
        try {
            Constructor<?> constructor = handlerClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Method> getControllerMethods(final Object controller) {
        return ReflectionUtils.getMethods(
                controller.getClass(),
                method -> method.isAnnotationPresent(RequestMapping.class)
        );
    }

    private void setHandlerExecutions(
            final Object handler, final Set<Method> methods, final Map<HandlerKey, HandlerExecution> handlerExecutions
    ) {
        for (Method mtd : methods) {
            List<HandlerKey> handlerKeys = createHandlerKeys(mtd);
            for (HandlerKey handlerKey : handlerKeys) {
                HandlerMethod handlerMethod = new HandlerMethod(handler, mtd);
                handlerExecutions.put(handlerKey, new HandlerExecution(handlerMethod));
            }
        }
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String path = annotation.value();
        return Arrays.stream(annotation.method())
                .map(m -> new HandlerKey(path, m))
                .collect(Collectors.toList());
    }
}

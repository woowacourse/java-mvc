package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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
        final Set<Class<?>> controllerClasses = getControllerClasses(basePackage);
        final Map<Class<?>, Set<Method>> handlerMethodsPerController = getMethodsPerController(controllerClasses);

        for (Entry<Class<?>, Set<Method>> handlerMethods : handlerMethodsPerController.entrySet()) {
            final Class<?> controllerClass = handlerMethods.getKey();
            final Set<Method> methods = handlerMethods.getValue();
            Map<Method, Set<HandlerKey>> handlerKeysPerMethod = getHandlerKeysPerMethod(methods);
            mapHandlerExecutions(controllerClass, handlerKeysPerMethod);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), requestMethod));
    }

    private Set<Class<?>> getControllerClasses(final Object[] basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<Class<?>, Set<Method>> getMethodsPerController(final Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .collect(Collectors.toMap(
                        clazz -> clazz,
                        clazz -> new Reflections(clazz, Scanners.MethodsAnnotated)
                                .getMethodsAnnotatedWith(RequestMapping.class)
                ));
    }

    private Map<Method, Set<HandlerKey>> getHandlerKeysPerMethod(final Set<Method> methods) {
        return methods.stream()
                .collect(Collectors.toMap(
                        method -> method,
                        HandlerKey::allFrom
                ));
    }

    private void mapHandlerExecutions(final Class<?> controllerClass,
                                      final Map<Method, Set<HandlerKey>> handlerKeysPerMethod) {
        final Object controller = getInstance(controllerClass);
        for (Entry<Method, Set<HandlerKey>> handlerKeys : handlerKeysPerMethod.entrySet()) {
            final Method method = handlerKeys.getKey();
            final Set<HandlerKey> keys = handlerKeys.getValue();
            keys.forEach(
                    handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(controller, method))
            );
        }
    }

    private Object getInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

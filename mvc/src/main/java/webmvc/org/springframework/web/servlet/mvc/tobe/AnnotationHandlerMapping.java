package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        final Map<Method, Object> controllerMethodAndObject = getControllerMethodAndObject(controllerClasses);
        initializeHandlerExecutions(controllerMethodAndObject);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Map<Method, Object> getControllerMethodAndObject(final Set<Class<?>> controllerClasses) {
        final Map<Method, Object> controllerMethodAndObject = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            final Method[] declaredMethods = controllerClass.getDeclaredMethods();
            final Object controller = createBean(controllerClass);
            final List<Method> methodsWithRequestMapping = findAllMethodsWithRequestMapping(declaredMethods);
            methodsWithRequestMapping.forEach(method -> controllerMethodAndObject.put(method, controller));
        }
        return controllerMethodAndObject;
    }

    private List<Method> findAllMethodsWithRequestMapping(final Method[] declaredMethods) {
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Object createBean(final Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = controllerClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("빈 객체 생성 중 에러");
        }
    }

    private void initializeHandlerExecutions(final Map<Method, Object> controllerMethodAndObject) {
        for (Map.Entry<Method, Object> methodAndObject : controllerMethodAndObject.entrySet()) {
            final Method method = methodAndObject.getKey();
            final Object controller = controllerMethodAndObject.get(method);
            final HandlerExecution handlerExecution = new HandlerExecution(method, controller);
            final List<HandlerKey> handlerKeys = getHandlerKeys(method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
        }
    }

    private List<HandlerKey> getHandlerKeys(final Method method) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final String value = annotation.value();
        final RequestMethod[] requestMethods = annotation.method();
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(value, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey matchingHandlerKey = handlerExecutions.keySet().stream()
                .filter(handlerKey -> handlerKey.isMatching(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("404"));
        return handlerExecutions.get(matchingHandlerKey);
    }
}

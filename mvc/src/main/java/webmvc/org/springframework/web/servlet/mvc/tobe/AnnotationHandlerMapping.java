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
        final List<Reflections> packageReflections = getPackageReflections();
        final List<Class<?>> controllerClasses = getControllerClasses(packageReflections);
        final Map<Method, Object> controllerMethodAndObject = getControllerMethodAndObject(controllerClasses);
        initializeHandlerExecutions(controllerMethodAndObject);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Reflections> getPackageReflections() {
        return Arrays.stream(basePackage)
                .map(Reflections::new)
                .collect(Collectors.toList());
    }

    private List<Class<?>> getControllerClasses(final List<Reflections> packageReflections) {
        final List<Class<?>> controllerClasses = new ArrayList<>();
        for (Reflections reflections : packageReflections) {
            final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            controllerClasses.addAll(classes);
        }
        return controllerClasses;
    }

    private Map<Method, Object> getControllerMethodAndObject(final List<Class<?>> controllerClasses) {
        final Map<Method, Object> controllerMethodAndObject = new HashMap<>();
        for (Class<?> controllerClass : controllerClasses) {
            final Method[] declaredMethods = controllerClass.getDeclaredMethods();
            final Constructor<?> constructor;
            final Object controller;
            try {
                constructor = controllerClass.getConstructor();
                controller = constructor.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("빈 객체 생성 중 에러");
            }
            final List<Method> controllerMethod = Arrays.stream(declaredMethods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());
            for (Method method : controllerMethod) {
                controllerMethodAndObject.put(method, controller);
            }
        }
        return controllerMethodAndObject;
    }

    private void initializeHandlerExecutions(final Map<Method, Object> controllerMethodAndObject) {
        for (Map.Entry<Method, Object> methodAndObject : controllerMethodAndObject.entrySet()) {
            final Method method = methodAndObject.getKey();
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            final Object controller = controllerMethodAndObject.get(method);
            final HandlerExecution handlerExecution = new HandlerExecution(method, controller);
            final String value = annotation.value();
            final RequestMethod[] requestMethods = annotation.method();
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey1 = handlerExecutions.keySet().stream()
                .filter(handlerKey -> handlerKey.isMatching(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("404"));
        return handlerExecutions.get(handlerKey1);
    }
}

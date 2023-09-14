package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
<<<<<<< HEAD
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
=======
>>>>>>> 6ce849f ([MVC 구현하기 - 1단계] 제이미(임정수) 미션 제출합니다. (#411))

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::putHandlerExecutions);
<<<<<<< HEAD
=======
    }

    private void putHandlerExecutions(final Class<?> clazz) {
        final List<Method> methods = getMethods(clazz);

        for (final Method method : methods) {
            final List<HandlerKey> handlerKeys = calculateHandlerKeys(method);
            handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method)));
        }
    }

    private static List<Method> getMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                     .filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }

    private List<HandlerKey> calculateHandlerKeys(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String value = requestMapping.value();
        return Arrays.stream(requestMapping.method())
                     .map(requestMethod -> new HandlerKey(value, requestMethod))
                     .collect(Collectors.toList());
>>>>>>> 6ce849f ([MVC 구현하기 - 1단계] 제이미(임정수) 미션 제출합니다. (#411))
    }

    private void putHandlerExecutions(final Class<?> clazz) {
        try {
            final List<Method> methods = processMethodsBy(clazz);

            final Object instance = clazz.getDeclaredConstructor().newInstance();

            for (final Method method : methods) {
                final List<HandlerKey> handlerKeys = processHandlerKeysBy(method);
                handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, new HandlerExecution(instance, method)));
            }
        } catch (Exception ex) {
            log.error("error : {}", ex);
        }
    }

    private List<Method> processMethodsBy(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                     .filter(clazz -> clazz.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }

    private List<HandlerKey> processHandlerKeysBy(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String value = requestMapping.value();
        return Arrays.stream(requestMapping.method())
                     .map(requestMethod -> new HandlerKey(value, requestMethod))
                     .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return handlerExecutions.get(handlerKey);
    }
}

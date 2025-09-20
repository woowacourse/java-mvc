package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
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
        scanBasePackages();

        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        final HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new NoSuchElementException("해당 요청을 처리할 수 있는 핸들러가 없습니다. : " + handlerKey);
        }

        return handlerExecution;
    }

    private void scanBasePackages() {
        for (final Object targetPackage : basePackage) {
            final Reflections reflections = new Reflections(targetPackage);
            final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            scanControllerClasses(controllerClasses);
        }
    }

    private void scanControllerClasses(final Set<Class<?>> controllerClasses) {
        for (final Class<?> controllerClass : controllerClasses) {
            final Object controllerInstance = getControllerInstance(controllerClass);
            final Method[] methods = controllerClass.getMethods();
            scanRequestMappingMethods(methods, controllerInstance);
        }
    }

    private Object getControllerInstance(final Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controllerClass);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("기본 생성자가 없습니다. : " + controllerClass);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ignore) {
        }

        throw new IllegalStateException("컨트롤러 인스턴스를 생성할 수 없습니다. : " + controllerClass);
    }

    private void scanRequestMappingMethods(
            final Method[] methods,
            final Object controllerInstance
    ) {
        for (final Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                final String value = annotation.value();
                final RequestMethod[] requestMethods = annotation.method();
                putHandlerExecution(controllerInstance, method, requestMethods, value);
            }
        }
    }

    private void putHandlerExecution(
            final Object controllerInstance,
            final Method method,
            final RequestMethod[] requestMethods,
            final String value
    ) {
        for (final RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final var reflections = new Reflections(basePackage);
        final var controllers = reflections.getTypesAnnotatedWith(
                context.org.springframework.stereotype.Controller.class
        );
        controllers.forEach(this::addControllerMethods);
    }

    private void addControllerMethods(Class<?> clazz) {
        final var controller = convertToControllerInstance(clazz);

        final var methods = clazz.getMethods();
        for (Method method : methods) {
            addAnnotatedWithRequestMapping(controller, method);
        }
    }

    private Object convertToControllerInstance(Class<?> clazz) {
        try {
            final var constructor = clazz.getDeclaredConstructor();

            return createControllerInstance(constructor);
        } catch (ClassCastException exception) {
            throw new HandlerMappingException("Class cannot be cast to Class<Controller>; " + clazz.getName(), exception);
        } catch (NoSuchMethodException exception) {
            throw new HandlerMappingException("Class does not have a default constructor;", exception);
        }
    }

    private Object createControllerInstance(Constructor<?> constructor) {
        try {
            return constructor.newInstance();
        } catch (InvocationTargetException exception) {
            throw new HandlerMappingException("Cannot invoke a constructor;", exception);
        } catch (InstantiationException exception) {
            throw new HandlerMappingException("Cannot instantiate an abstract class or interface;", exception);
        } catch (IllegalAccessException exception) {
            throw new HandlerMappingException("Cannot access to a constructor;", exception);
        }
    }

    private void addAnnotatedWithRequestMapping(Object controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            addHandlerExecution(controller, method);
        }
    }

    private void addHandlerExecution(Object controller, Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var mappingUrl = requestMapping.value();
        final var mappingRequestMethods = requestMapping.method();

        for (RequestMethod requestMethod : mappingRequestMethods) {
            handlerExecutions.put(new HandlerKey(mappingUrl, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Optional<HandlerExecution> getHandler(final HttpServletRequest request) {
        final var key = new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod()));

        return Optional.ofNullable(handlerExecutions.get(key));
    }
}

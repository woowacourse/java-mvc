package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        log.info("Initializing AnnotationHandlerMapping starts!");
        for (Object base : basePackage) {
            registerFromBasePackage((String) base);
        }
        log.info("Initializing AnnotationHandlerMapping succeeds!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        try {
            RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            return handlerExecutions.get(handlerKey);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported request method.");
            throw e;
        }
    }

    public void registerFromBasePackage(String basePackage) {
        Set<Class<?>> handlers = scanAnnotatedHandler(basePackage);
        for (Class<?> handler : handlers) {
            Object instance = getInstance(handler);
            List<Method> methods = getRequestMappingMethods(handler);
            register(instance, methods);
        }
    }

    private Set<Class<?>> scanAnnotatedHandler(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> noArgumentConstructor = getNoArgumentConstructor(clazz);
            noArgumentConstructor.setAccessible(true);
            return noArgumentConstructor.newInstance();
        } catch (IllegalAccessException e) {
            log.error("Constructor is not accessible.");
        } catch (IllegalArgumentException e) {
            log.error("Type of Arguments doesn't matched.");
        } catch (InstantiationException e) {
            log.error("The instance is abstract class.");
        } catch (InvocationTargetException e) {
            log.error("Exception occurs during constructing.");
        } catch (ExceptionInInitializerError error) {
            log.error("Initializing fails.");
        }
        throw new IllegalArgumentException("Getting instance using constructor fails.");
    }

    private Constructor<?> getNoArgumentConstructor(Class<?> clazz) {
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            int parameterCount = declaredConstructor.getParameterCount();
            if (parameterCount == 0) {
                return declaredConstructor;
            }
        }
        throw new IllegalArgumentException("Handler doesn't have no argument constructor");
    }

    private List<Method> getRequestMappingMethods(Class<?> handler) {
        return Arrays.stream(handler.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void register(Object instance, List<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            if (isInvalidRequestMapping(url, requestMethods)) {
                continue;
            }
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info("uri = {}, method = {}, name = {} is mapped!", url, requestMethod.name(), method.getName());
            }
        }
    }

    private boolean isInvalidRequestMapping(String url, RequestMethod[] requestMethods) {
        return url.isBlank() || requestMethods.length == 0;
    }
}

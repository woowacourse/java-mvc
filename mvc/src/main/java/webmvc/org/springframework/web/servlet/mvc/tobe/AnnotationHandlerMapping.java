package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> handlerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> handlerClazz : handlerClazzSet) {
            setupHandlerExecutions(handlerClazz);
        }
    }

    private void setupHandlerExecutions(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                setupRequestMappingIntoHandlerExecution(clazz, method);
            }
        }
    }

    private void setupRequestMappingIntoHandlerExecution(Class<?> clazz, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        RequestMethod[] requestMappingMethods = requestMapping.method();
        String requestMappingPath = requestMapping.value();

        HandlerExecution handlerExecution = new HandlerExecution(instantiate(clazz), method);

        for (RequestMethod requestMappingMethod : requestMappingMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMappingPath, requestMappingMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object instantiate(Class<?> clazz) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new InstantiationFailedException();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

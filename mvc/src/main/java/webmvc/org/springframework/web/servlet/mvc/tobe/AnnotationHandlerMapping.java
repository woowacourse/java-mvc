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
import java.lang.reflect.InvocationTargetException;
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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            try {
                Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controller);
                ReflectionUtils.makeAccessible(constructor);
                Object instance = constructor.newInstance(null);

                Method[] methods = instance.getClass()
                        .getDeclaredMethods();

                for (Method method : methods) {

                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

                        RequestMethod[] requestMethods = requestMapping.method();
                        String requestURL = requestMapping.value();

                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(requestURL, requestMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(instance);

                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }

            } catch (NoSuchMethodException | InstantiationException |
                     IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

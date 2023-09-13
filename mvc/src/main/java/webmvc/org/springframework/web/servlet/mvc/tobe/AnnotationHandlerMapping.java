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
        Set<Class<?>> controllerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClazz : controllerClazzSet) {
            Object controller = instantiate(controllerClazz);
            setupHandlerExecutions(controller);
        }
    }

    private static Object instantiate(Class<?> controller) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(controller);
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new InstantiationFailedException();
    }

    private void setupHandlerExecutions(Object controller) {
        Method[] methods = controller.getClass()
                .getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                setupHandlerExecution(controller, method);
            }
        }
    }

    private void setupHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = requestMapping.method();
        String requestURL = requestMapping.value();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestURL, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

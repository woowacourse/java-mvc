package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            initHandlerExecutions(clazz);
        }
        logInitializedRequestPath();
    }

    private void initHandlerExecutions(final Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                initHandlerExecutions(clazz, method, requestMapping);
            }
        }
    }

    private void initHandlerExecutions(final Class<?> clazz, final Method declaredMethod,
                                       final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerExecution handlerExecution = instantiateHandlerExecution(clazz, declaredMethod);
            handlerExecutions.add(new HandlerKey(uri, requestMethod), handlerExecution);
        }
    }

    private HandlerExecution instantiateHandlerExecution(final Class<?> clazz, final Method method) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return new HandlerExecution(constructor.newInstance(), method);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            log.error("fail initialize!");
            throw new RuntimeException(e);
        }
    }

    private void logInitializedRequestPath() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.getHandlers()
                .forEach(handlerKey -> log.info("Path : {}", handlerKey));
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.getHandlerExecution(request.getRequestURI(), request.getMethod());
    }
}

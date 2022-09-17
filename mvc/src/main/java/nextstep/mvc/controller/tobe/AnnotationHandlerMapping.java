package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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
        Set<Class<?>> classes = getControllerClasses();
        for (Class<?> clazz : classes) {
            initHandlerExecutions(clazz);
        }
        log();
    }

    private Set<Class<?>> getControllerClasses() {
        Set<Class<?>> classes = new HashSet<>();
        for (Object packageName : basePackage) {
            Reflections reflections = new Reflections(packageName);
            classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }
        return classes;
    }

    private void initHandlerExecutions(final Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method declaredMethod : methods) {
            if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
                initHandlerExecutions(clazz, declaredMethod, requestMapping);
            }
        }
    }

    private void initHandlerExecutions(final Class<?> clazz, final Method declaredMethod, final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] method = requestMapping.method();
        for (RequestMethod requestMethod : method) {
            HandlerExecution handlerExecution = instantiateHandlerExecution(clazz, declaredMethod);
            handlerExecutions.put(new HandlerKey(uri, requestMethod), handlerExecution);
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

    private void log() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Path : {}", handlerKey));
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}

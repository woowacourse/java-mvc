package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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
        for (Object packageName : basePackage) {
            extractClass(packageName);
        }
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.keySet()
                .forEach(handlerKey -> log.info("Path : {}", handlerKey));
    }

    private void extractClass(final Object packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            extractMethods(clazz);
        }
    }

    private void extractMethods(final Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            extractedAnnotation(clazz, declaredMethod);
        }
    }

    private void extractedAnnotation(final Class<?> clazz, final Method declaredMethod) {
        if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);
            initHandlerExecution(clazz, declaredMethod, requestMapping);
        }
    }

    private void initHandlerExecution(final Class<?> clazz,
                                      final Method declaredMethod,
                                      final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] method = requestMapping.method();
        addHandlerExecutions(clazz, declaredMethod, uri, method[0]);
    }

    private void addHandlerExecutions(final Class<?> clazz,
                                      final Method declaredMethod,
                                      final String uri,
                                      final RequestMethod method) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            HandlerExecution handlerExecution = new HandlerExecution(constructor.newInstance(), declaredMethod);
            handlerExecutions.put(new HandlerKey(uri, method), handlerExecution);
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            log.error("fail initialize!");
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}

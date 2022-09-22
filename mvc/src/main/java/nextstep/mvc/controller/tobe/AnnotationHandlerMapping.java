package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.NotFoundHandlerException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<RequestMapping> REQUEST_MAPPING_ANNOTATION_CLASS = RequestMapping.class;
    private static final Class<Controller> CONTROLLER_ANNOTATION_CLASS = Controller.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CONTROLLER_ANNOTATION_CLASS);

        for (Class<?> clazz : classes) {
            Object instance = getInstance(clazz);
            final List<Method> methods = getRequestMappingMethods(clazz);
            methods.forEach(method -> putHandlerExecutionByRequestMapping(instance, method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object getInstance(final Class<?> clazz) {
        try {
            final Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException exception) {
            log.error(exception.getMessage());
            throw new IllegalArgumentException("생성자를 가져올 수 없습니다. " + clazz.getName());
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException exception){
            log.error(exception.getMessage());
            throw new IllegalArgumentException("인스턴스화할 수 없습니다. " + clazz.getName());
        }
    }

    private List<Method> getRequestMappingMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_ANNOTATION_CLASS))
                .collect(Collectors.toList());
    }

    private void putHandlerExecutionByRequestMapping(final Object instance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_ANNOTATION_CLASS);
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }

        throw new NotFoundHandlerException(requestURI, method);
    }
}

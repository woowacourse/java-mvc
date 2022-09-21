package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;
    private static final Class<Controller> CONTROLLER_CLASS = Controller.class;
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Class<?> controllerClass : findControllers()) {
            Object handler = getInstance(controllerClass);
            findMappingMethods(controllerClass)
                    .forEach(method -> saveMethodMapping(handler, method));
        }
    }

    public List<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(CONTROLLER_CLASS);
        return new ArrayList<>(controllers);
    }

    public List<Method> findMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_CLASS))
                .collect(Collectors.toList());
    }

    private void saveMethodMapping(Object handler, Method method) {
        RequestMapping annotation = method.getAnnotation(REQUEST_MAPPING_CLASS);
        for (RequestMethod requestMethod : annotation.method()) {
            String requestURL = annotation.value();
            HandlerKey handlerKey = new HandlerKey(requestURL, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method : {}.{}()", requestURL, method.getDeclaringClass(), method.getName());
        }
    }

    private Object getInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("인스턴스를 찾을 수 없습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, method));
    }
}

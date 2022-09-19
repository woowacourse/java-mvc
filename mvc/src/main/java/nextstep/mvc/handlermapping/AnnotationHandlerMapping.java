package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            Set<Class<?>> controllerClasses = getControllerClasses(basePackage);

            addHandlerExecutions(controllerClasses);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> getControllerClasses(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void addHandlerExecutions(Set<Class<?>> controllerClasses) {
        for (Class<?> controllerClass : controllerClasses) {
            addHandlerExecution(controllerClass);
        }
    }

    private void addHandlerExecution(Class<?> controllerClass) {
        List<Method> requestMappingMethods = findRequestMappingMethods(controllerClass);

        for (Method method : requestMappingMethods) {
            Set<HandlerKey> handlerKeys = createHandlerKeys(method);
            final Object controller = createController(controllerClass);
            handlerKeys.forEach(key -> handlerExecutions.put(key, new HandlerExecution(controller, method)));
        }
    }

    private List<Method> findRequestMappingMethods(Class<?> controllerClass) {
        Method[] methods = controllerClass.getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Set<HandlerKey> createHandlerKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toSet());
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {

            log.warn(controllerClass + " 객체를 생성할 수 없습니다.");
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

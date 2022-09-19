package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);

        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            putHandlersByEachController(controllerClass);
        }
    }

    private void putHandlersByEachController(final Class<?> controllerClass) {
        final Object controller = createControllerObject(controllerClass);

        for (Method method : findRequestMappingMethods(controllerClass)) {
            putHandlersByEachMethod(method, controller);
        }
    }

    private Object createControllerObject(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(controllerClass.getName() + "의 컨트롤러 인스턴스를 생성할 수 없습니다.", e);
        }
    }

    private List<Method> findRequestMappingMethods(final Class<?> controllerClass) {
        return Stream.of(controllerClass.getDeclaredMethods())
                .filter(this::isHandleableMethod)
                .collect(Collectors.toList());
    }

    private boolean isHandleableMethod(final Method method) {
        return method.isAnnotationPresent(RequestMapping.class) && isHandleableMethodParameters(method);
    }

    private boolean isHandleableMethodParameters(final Method method) {
        final List<Class<?>> currentMethodParameters = List.of(method.getParameterTypes());
        final List<Class<?>> handleableMethodParameters = List.of(HttpServletRequest.class, HttpServletResponse.class);

        return currentMethodParameters.equals(handleableMethodParameters);
    }

    private void putHandlersByEachMethod(final Method method, final Object controller) {
        final HandlerExecution execution = new HandlerExecution(method, controller);

        for (HandlerKey handlerKey : findHandlerKeys(method)) {
            log.info("HandlerKey : {}", handlerKey);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    private List<HandlerKey> findHandlerKeys(Method handlerMethod) {
        final RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();

        return Stream.of(requestMapping.method())
                .map(method -> new HandlerKey(url, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String requestMethodName = request.getMethod().toUpperCase();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(requestMethodName));
        return handlerExecutions.get(handlerKey);
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerType : controllerTypes) {
            initializeFromControllerType(controllerType);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeFromControllerType(final Class<?> controllerType) {
        try {
            final Object controller = controllerType.getDeclaredConstructor().newInstance();
            for (final Method method : controllerType.getDeclaredMethods()) {
                if (!containsRequestMappingAnnotation(method)) {
                    continue;
                }
                initializeFromMethod(method, controller);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalStateException(String.format("컨트롤러를 인스턴스화 할 수 없습니다.(%s)", controllerType.getName()));
        }
    }

    private boolean containsRequestMappingAnnotation(final Method method) {
        return Arrays.stream(method.getDeclaredAnnotations())
            .anyMatch(annotation -> annotation.annotationType().equals(REQUEST_MAPPING_CLASS));
    }

    private void initializeFromMethod(final Method method, final Object controller) {
        final RequestMapping annotation = method.getAnnotation(REQUEST_MAPPING_CLASS);
        for (RequestMethod requestMethod : annotation.method()) {
            handlerExecutions.put(new HandlerKey(annotation.value(), requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}

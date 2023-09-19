package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        getControllersMethods().forEach(this::addToHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> getControllersMethods() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        return controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addToHandlerExecutions(final Method method) {
        Object controller = getController(method.getDeclaringClass());
        final HandlerExecution handlerExecution = new HandlerExecution(method, controller);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private Object getController(Class<?> controller) {
        try {
            final Constructor<?> constructor = controller.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.warn("controller 생성 중 문제가 발생했습니다.");
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

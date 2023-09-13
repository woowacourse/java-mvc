package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        for (final Class<?> controller : getClassesWithControllerAnnotated(reflections)) {
            putHandlerByController(controller);
        }
    }

    private Set<Class<?>> getClassesWithControllerAnnotated(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void putHandlerByController(final Class<?> controller) {
        Arrays.stream(controller.getMethods())
                .filter(hasAnnotatedWithRequestMapping())
                .forEach(this::putHandlerExecutionsByMethod);
    }

    private Predicate<Method> hasAnnotatedWithRequestMapping() {
        return method -> method.isAnnotationPresent(RequestMapping.class);
    }

    private void putHandlerExecutionsByMethod(final Method method) {
        HandlerExecution handlerExecution = new HandlerExecution(method);

        for (HandlerKey handlerKey : findHandlerKeys(method)) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> findHandlerKeys(final Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        return Arrays.stream(requestMapping.method())
                .map(method -> new HandlerKey(url, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(request.getMethod())));
    }
}

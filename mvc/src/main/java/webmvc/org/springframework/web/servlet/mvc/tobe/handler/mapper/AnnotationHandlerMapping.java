package webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper;

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
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);

        for (final Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            putHandlerByController(controller);
        }

        handlerExecutions.keySet()
                .forEach(key -> log.info("Annotated Based Controller : {}", key.toString()));
    }

    private void putHandlerByController(final Class<?> controller) {
        Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(this::putHandlerExecutionsByMethodAnnotatedWithRequestMapping);
    }

    private void putHandlerExecutionsByMethodAnnotatedWithRequestMapping(final Method handlerMethod) {
        HandlerExecution handlerExecution = new HandlerExecution(handlerMethod);

        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);

        String url = requestMapping.value();
        Arrays.stream(requestMapping.method())
                .map(method -> new HandlerKey(url, method))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(request.getMethod())));
    }
}

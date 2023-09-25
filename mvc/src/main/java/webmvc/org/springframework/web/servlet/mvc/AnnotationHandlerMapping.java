package webmvc.org.springframework.web.servlet.mvc;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;

import static java.util.stream.Collectors.toList;

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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            putHandlerExecutionOfController(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutionOfController(Class<?> controller) {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            putHandlerExecutionOfMethod(controller, method);
        }
    }

    private void putHandlerExecutionOfMethod(Class<?> controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            putHandlerExecutionOfHandlerKeys(handlerKeys, handlerExecution);
        }
    }

    private List<HandlerKey> createHandlerKeys(RequestMapping requestMapping) {
        String uri = requestMapping.value();
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(uri, requestMethod))
                .collect(toList());
    }

    private void putHandlerExecutionOfHandlerKeys(List<HandlerKey> handlerKeys, HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
            handlerExecutions.keySet()
                    .forEach(path -> log.info("Path : {}, HandlerMapping : {}", path, handlerExecutions.get(handlerKey).getClass()));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

}

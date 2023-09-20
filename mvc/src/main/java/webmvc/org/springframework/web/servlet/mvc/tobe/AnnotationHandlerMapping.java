package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        for (Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            final List<Method> methods = Arrays.stream(controller.getMethods())
                                               .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                                               .collect(Collectors.toList());
            for (Method method : methods) {
                final HandlerKey handlerKey = new HandlerKey(
                        method.getAnnotation(RequestMapping.class).value(),
                        method.getAnnotation(RequestMapping.class).method()[0]);
                try {
                    final Object instance = controller.getDeclaredConstructor().newInstance();
                    final HandlerExecution handlerExecution = new HandlerExecution(method, instance);
                    handlerExecutions.put(handlerKey, handlerExecution);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        return handlerExecutions.getOrDefault(
                new HandlerKey(
                        request.getRequestURI(),
                        RequestMethod.valueOf(request.getMethod())
                )
        , null);
    }
}

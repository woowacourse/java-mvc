package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        getExecutionMethods()
            .forEach(this::addHandlerExecution);
    }

    private void addHandlerExecution(Method method) {
        RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        String path = annotation.value();

        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));

            log.info("Handler : {} {}", requestMethod.name(), path);
        }
    }

    private List<Method> getExecutionMethods() {
        List<Method> executionMethods = new ArrayList<>();

        for (Object packageName : basePackage) {
            Reflections reflections = new Reflections(packageName);
            List<Method[]> declaredMethods = reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(Class::getDeclaredMethods)
                .collect(Collectors.toList());

            addAnnotatedMethods(executionMethods, declaredMethods);
        }
        return executionMethods;
    }

    private void addAnnotatedMethods(List<Method> executionMethods, List<Method[]> foundMethods) {
        for (Method[] methods : foundMethods) {
            Arrays.stream(methods)
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .forEach(executionMethods::add);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutions.entrySet().stream()
            .filter(it -> it.getKey().isMatchingKey(request))
            .map(Entry::getValue)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}

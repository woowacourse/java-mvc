package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
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
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

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
        log.info("Initialized AnnotationHandlerMapping!");
        collectAllControllerClasses().forEach(this::generateHandlerExecutions);
    }

    private Set<Class<?>> collectAllControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void generateHandlerExecutions(final Class<?> controllerClass) {
        final Object controller = generateInstance(controllerClass);
        final Method[] methods = controllerClass.getMethods();
        for (Method method : methods) {
            generateHandlerExecutions(controller, method);
        }
    }

    private Object generateInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("인스턴스 생성에 실패하였습니다.");
        }
    }

    private void generateHandlerExecutions(final Object controller, final Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }

        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String path = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        final List<HandlerKey> handlerKeys = generateHandlerKeys(path, requestMethods);
        final HandlerExecution handlerExecution = new AnnotationHandlerExecution(controller, method);
        handlerKeys.forEach(key -> handlerExecutions.put(key, handlerExecution));
    }

    private List<HandlerKey> generateHandlerKeys(final String path, final RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .collect(Collectors.toList());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.getOrDefault(handlerKey, null);
    }
}

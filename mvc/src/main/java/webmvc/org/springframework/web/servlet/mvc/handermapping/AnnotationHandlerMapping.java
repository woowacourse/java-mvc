package webmvc.org.springframework.web.servlet.mvc.handermapping;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.handlerAdaptor.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.handlerAdaptor.HandlerKey;

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

        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            final List<Method> declaredMethods = getDeclaredRequestMapping(controllerClass);
            addHandlerExecution(controllerClass, declaredMethods);
        }
    }

    private List<Method> getDeclaredRequestMapping(final Class<?> controllerClass) {
        final Method[] methods = controllerClass.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecution(final Class<?> controllerClass, final List<Method> declaredMethods) {
        final String rootPath = getRootPath(controllerClass);
        for (Method declaredMethod : declaredMethods) {
            final List<HandlerKey> handlerKeys = getHandlerKeys(rootPath, declaredMethod);
            final HandlerExecution handlerExecution = getHandlerExecution(controllerClass, declaredMethod);
            handlerKeys.forEach(key -> handlerExecutions.put(key, handlerExecution));
        }
    }

    private HandlerExecution getHandlerExecution(final Class<?> controllerClass, final Method declaredMethod) {
        try {
            return HandlerExecution.of(controllerClass, declaredMethod);
        } catch (Exception e) {
            throw new NoSuchElementException("Class 를 확인해주세요.");
        }
    }

    private String getRootPath(final Class<?> controllerClass) {
        return controllerClass.getAnnotation(Controller.class).path();
    }

    private List<HandlerKey> getHandlerKeys(final String rootPath, final Method declaredMethod) {
        final RequestMapping requestMappingAnnotation = declaredMethod.getAnnotation(RequestMapping.class);

        return Arrays.stream(requestMappingAnnotation.method())
                .map(requestMethod -> new HandlerKey(rootPath + requestMappingAnnotation.value(), requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKeyByRequest = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));

        final HandlerExecution handlerExecution = handlerExecutions.get(handlerKeyByRequest);

        if (Objects.isNull(handlerExecution)) {
            return handlerExecutions.get(new HandlerKey("/404", RequestMethod.GET));
        }
        return handlerExecution;
    }
}

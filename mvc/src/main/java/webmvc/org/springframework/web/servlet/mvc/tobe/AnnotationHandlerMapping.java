package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        classes.forEach(aClass -> {
            final Object controllerInstance = getControllerInstance(aClass);
            final List<Method> requestMappingAnnotatedMethod = findRequestMappingAnnotatedMethod(aClass);

            requestMappingAnnotatedMethod
                    .forEach(method -> putHandlerByMethod(controllerInstance, method));
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object getControllerInstance(final Class<?> aClass) {
        try {
            return aClass.getConstructor().newInstance();
        } catch (Exception exception) {
            log.error("{} 인스턴스 생성 실패", aClass.getSimpleName());
            throw new IllegalStateException(aClass.getSimpleName() + "의 생성자를 찾을수 없습니다.");
        }
    }

    private List<Method> findRequestMappingAnnotatedMethod(final Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void putHandlerByMethod(final Object controllerInstance, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] methods = requestMapping.method();
        final String path = requestMapping.value();
        Arrays.stream(methods)
                .forEach(requestMethod -> putHandler(controllerInstance, method, path, requestMethod));
    }

    private void putHandler(final Object controllerInstance,
                           final Method method,
                           final String path,
                           final RequestMethod requestMethod) {
        final HandlerKey key = new HandlerKey(path, requestMethod);
        final HandlerExecution execution = new HandlerExecution(method, controllerInstance);
        handlerExecutions.put(key, execution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

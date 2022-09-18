package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
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

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final HandlerKey ROOT_HANDLER_KEY = new HandlerKey("/", RequestMethod.GET);

    private final List<String> basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = Arrays.stream(basePackage)
            .collect(Collectors.toUnmodifiableList());
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (String path : basePackage) {
            final Reflections reflections = new Reflections(path);
            final Set<Class<?>> classesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

            // Controller Annotation이 붙은 클래스들
            for (Class<?> clazz : classesAnnotatedWith) {
                Object controller;
                try {
                    controller = clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException e) {
                    throw new IllegalArgumentException("");
                }

                final Method[] methods = clazz.getMethods();

                // 클래스내 메서드들
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        final String requestPath = requestMapping.value();
                        final RequestMethod[] requestMethods = requestMapping.method();
                        for (RequestMethod requestMethod : requestMethods) {
                            final HandlerKey handlerKey = new HandlerKey(requestPath, requestMethod);
                            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
                        }
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod()));
        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        return handlerExecutions.get(ROOT_HANDLER_KEY);
    }
}

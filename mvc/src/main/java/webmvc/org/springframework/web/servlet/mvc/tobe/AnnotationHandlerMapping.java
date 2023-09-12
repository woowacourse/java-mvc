package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;

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

        final var controllers = Arrays.stream(basePackage)
                .flatMap(obj -> new Reflections(obj).getTypesAnnotatedWith(Controller.class).stream())
                .map(this::getObject)
                .collect(Collectors.toList());
        for (Object controller: controllers) {
            Map<HandlerKey, HandlerExecution> collect = Arrays.stream(
                            controller.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .flatMap(method -> getEntryStream(controller, method))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            handlerExecutions.putAll(collect);
        }
    }

    private Object getObject(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    class OverridenHandlerExecution extends HandlerExecution {

        private Object instance;
        private Method handler;

        public OverridenHandlerExecution(Object instance, Method handler) {
            this.instance = instance;
            this.handler = handler;
        }

        @Override
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            return (ModelAndView) handler.invoke(instance, request, response);
        }
    }

    private Stream<Map.Entry<HandlerKey, HandlerExecution>> getEntryStream(final Object controller, final Method method) { // request mapping method
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] methods = requestMapping.method();
        return Stream.of(methods)
                .map(m -> Map.entry(new HandlerKey(requestMapping.value(), m), new OverridenHandlerExecution(controller, method)));
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        final var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

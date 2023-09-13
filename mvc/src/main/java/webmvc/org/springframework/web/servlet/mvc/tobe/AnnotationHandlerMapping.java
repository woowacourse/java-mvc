package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private HandlerExecution defaultHandlerExecution;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException {
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::putHandlerMethodInController);
        final Method defaultHandler = this.getClass()
                .getDeclaredMethod(
                        "defaultHandler",
                        HttpServletRequest.class,
                        HttpServletResponse.class
                );
        defaultHandlerExecution = new HandlerExecution(this.getClass(), defaultHandler);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerMethodInController(final Class<?> clazz) {
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> putHandlerMethodPerRequestMethod(clazz, method));
    }

    private void putHandlerMethodPerRequestMethod(final Class<?> controller, final Method handler) {
        final RequestMapping annotation = handler.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = annotation.method();
        Arrays.stream(requestMethods)
                .forEach(requestMethod ->
                        handlerExecutions.put(
                                new HandlerKey(annotation.value(), requestMethod),
                                new HandlerExecution(controller, handler)
                        )
                );
    }

    public Object getHandler(final HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.getOrDefault(handlerKey, defaultHandlerExecution);
    }

    private Object defaultHandler(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("요청하신 HTTP 메서드와 경로에 매핑되는 핸들러가 존재하지 않습니다.");
        return null;
    }
}

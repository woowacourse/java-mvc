package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final ClassScanner classScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.classScanner = new ClassScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final List<Object> handlers = classScanner.findInstanceByAnnotation(Controller.class);

        for (final Object handler : handlers) {
            final Method[] methods = handler.getClass()
                                            .getDeclaredMethods();

            processHandlerExecutors(handler, methods);
        }
    }

    private void processHandlerExecutors(final Object handler, final Method[] methods) {
        for (final Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                putHandlerExecutors(handler, method, requestMapping);
            }
        }
    }

    private void putHandlerExecutors(final Object handler, final Method method, final RequestMapping requestMapping) {
        for (final RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(handler, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, method);

        return handlerExecutions.get(handlerKey);
    }
}

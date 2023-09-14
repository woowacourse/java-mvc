package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllers) {
            processRequestMappingMethod(controller);
        }
    }

    private void processRequestMappingMethod(final Class<?> controller) {
        final Method[] methods = controller.getDeclaredMethods();

        for (final Method method : methods) {
            final Object handler = createInstanceOf(controller);

            processHandlerExecutors(handler, method);
        }
    }

    private Object createInstanceOf(final Class<?> controller) {
        try {
            return controller.getConstructor()
                             .newInstance();
        } catch (final NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e) {
            log.error("", e);
        }

        throw new IllegalArgumentException("해당 Handler를 생성할 수 없습니다.");
    }

    private void processHandlerExecutors(final Object handler, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            for (final RequestMethod requestMethod : requestMapping.method()) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                final HandlerExecution handlerExecution = new HandlerExecution(handler, method);

                handlerExecutions.put(handlerKey, handlerExecution);
            }
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

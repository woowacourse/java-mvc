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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

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
            processHandlerExecutors(controller, method);
        }
    }

    private void processHandlerExecutors(final Class<?> controller, final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            final HandlerExecution handlerExecution = calculateHandlerExecution(controller, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private HandlerExecution calculateHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Object handler = controller.getConstructor()
                                             .newInstance();

            return new HandlerExecution(handler, method);
        } catch (final NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException e)
        {
            log.error("", e);
        }

        throw new IllegalArgumentException("해당 Handler의 Mapping 정보를 처리할 수 없습니다.");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod method = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, method);

        return handlerExecutions.get(handlerKey);
    }
}

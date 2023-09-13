package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (final Object basePackage : basePackages) {
            final Reflections reflections = new Reflections(basePackage);
            final Set<Class<?>> controllerClazzSet = reflections.getTypesAnnotatedWith(Controller.class);
            for (final Class<?> clazz : controllerClazzSet) {
                for (final Method method : clazz.getDeclaredMethods()) {
                    final RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                    if (requestMappingAnnotation != null) {
                        final HandlerKey handlerKey =
                                new HandlerKey(requestMappingAnnotation.value(), requestMappingAnnotation.method()[0]);
                        final Object controller = getControllerInstance(clazz);
                        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
    }

    private Object getControllerInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalArgumentException("인스턴스를 찾을 수 없습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey =
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

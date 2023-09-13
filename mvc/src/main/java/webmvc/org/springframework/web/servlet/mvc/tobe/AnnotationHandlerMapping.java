package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllerClasses) {
            final Object controllerInstance = findDefaultConstructor(controller);
            final Method[] classMethods = controller.getDeclaredMethods();
            for (final Method classMethod : classMethods) {
                if (classMethod.isAnnotationPresent(RequestMapping.class)) {
                    final RequestMapping requestMapping = classMethod.getAnnotation(RequestMapping.class);
                    final String httpRequestURI = requestMapping.value();
                    final RequestMethod[] httpRequestMethods = requestMapping.method();
                    for (final RequestMethod httpRequestMethod : httpRequestMethods) {
                        final HandlerKey handlerKey = new HandlerKey(httpRequestURI, httpRequestMethod);
                        final HandlerExecution handlerExecution = HandlerExecution.of(controllerInstance, classMethod);

                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object findDefaultConstructor(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Controller 내에 기본 생성자가 없습니다!");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String method = request.getMethod();
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

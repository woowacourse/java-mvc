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
import java.util.Objects;
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
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            initializeByController(controller);
        }
    }

    private void initializeByController(final Class<?> controller) {
        for (final Method method : controller.getDeclaredMethods()) {
            initializeByMethod(controller, method);
        }
    }

    private void initializeByMethod(final Class<?> controller, final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            addHandlerExecution(controller, method, requestMapping);
        }
    }

    private void addHandlerExecution(final Class<?> controller, final Method method, final RequestMapping requestMapping) {
        final RequestMethod requestMethod = requestMapping.method()[0];
        final String value = requestMapping.value();
        try {
            final Object controllerInstance = controller.getConstructor().newInstance();
            handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(controllerInstance, method));
        } catch (Exception e) {
            log.error("{} 클래스를 생성할 수 없습니다.", controller.getSimpleName());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

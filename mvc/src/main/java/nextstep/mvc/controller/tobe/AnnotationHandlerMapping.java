package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final List<Object> controllers = controllerScanner.scan();
        controllers.forEach(this::init);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void init(final Object controller) {
        final Class<?> clazz = controller.getClass();
        final Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class));
        for (final Method method : methods) {
            final var requestMapping = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                    new HandlerKey(requestMapping.value(), requestMapping.method()[0]),
                    new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final var url = request.getRequestURI();
        final var method = request.getMethod();
        final var handlerKey = new HandlerKey(url, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

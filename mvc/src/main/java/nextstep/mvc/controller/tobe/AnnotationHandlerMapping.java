package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.InitializingFailedException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

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
        for (Class<?> controller : controllers) {
            initHandlerExecutions(controller);
        }
        log.info("Initialized Annotation Handler Mapping!");
        controllers
                .forEach(controller -> log.info("Controller : {}", controller.getCanonicalName()));
    }

    private void initHandlerExecutions(final Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            initHandlerExecutions(controller, method);
        }
    }

    private void initHandlerExecutions(final Class<?> controller, final Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        try {
            final Object instance = controller.getConstructor().newInstance();
            addHandlerExecutions(requestMapping, new HandlerExecution(instance, method));
        } catch (InstantiationException | InvocationTargetException
                 | NoSuchMethodException | IllegalAccessException e) {
            log.error("Initializing AnnotationHandlerMapping failed! - {}", controller.getCanonicalName(), e);
            throw new InitializingFailedException();
        }
    }

    private void addHandlerExecutions(final RequestMapping requestMapping, final HandlerExecution handlerExecution) {
        for (HandlerKey handlerKey : HandlerKey.from(requestMapping)) {
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

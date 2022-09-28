package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            initExecutions();
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initExecutions() throws Exception {
        ControllerScanner controllerScanner = ControllerScanner.from(basePackages);
        final Map<Class<?>, Object> controllers = controllerScanner.findControllers();
        controllers.forEach(this::createControllerExecutions);
    }

    private void createControllerExecutions(final Class<?> controller, final Object instance) {
        for (final Method method : getRequestMappingMethods(controller)) {
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(handlerExecution, requestMapping);
        }
    }

    private List<Method> getRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final HandlerExecution handlerExecution, final RequestMapping requestMapping) {
        final Set<HandlerKey> handlerKeys = Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .collect(Collectors.toSet());
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}

package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.controllerScanner = new ControllerScanner(basePackages);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final List<Object> controllers = controllerScanner.getControllers();
        addHandlerExecutions(controllers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(List<Object> controllers) {
        for (Object controller : controllers) {
            addHandlerExecutions(controller);
        }
    }

    private void addHandlerExecutions(Object controller) {
        Set<Method> requestMappingMethods = findRequestMappingMethods(controller);

        for (Method method : requestMappingMethods) {
            Set<HandlerKey> handlerKeys = createHandlerKeys(method);
            handlerKeys.forEach(key -> handlerExecutions.put(key, new HandlerExecution(controller, method)));
        }
    }

    private Set<Method> findRequestMappingMethods(Object controller) {
        return ReflectionUtils.getAllMethods(controller.getClass(),
                ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private Set<HandlerKey> createHandlerKeys(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toSet());
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RequestMethod requestMethod = RequestMethod.valueOf(method);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

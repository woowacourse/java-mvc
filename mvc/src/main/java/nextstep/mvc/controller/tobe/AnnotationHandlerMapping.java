package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.handler.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = ControllerScanner.from(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Class<?> controller : controllerScanner.findAllControllers()) {
            saveHandlers(controllerScanner.findMethodsWithRequestMapping(controller));
        }
    }

    private void saveHandlers(List<Method> methods) {
        for (Method method : methods) {
            saveHandler(method);
        }
    }

    private void saveHandler(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        List<HandlerKey> handlerKeys = extractToRequestMapping(method);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> extractToRequestMapping(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(uri, method);
        return handlerExecutions.get(handlerKey);
    }
}

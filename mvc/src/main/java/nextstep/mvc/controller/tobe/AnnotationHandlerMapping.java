package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.scanner.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        try {
            ControllerScanner controllerScanner = new ControllerScanner(basePackage);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            Set<Map.Entry<Class<?>, Object>> controllerEntry = controllers.entrySet();

            for (Map.Entry<Class<?>, Object> controller : controllerEntry) {
                Method[] declaredMethods = controller.getKey().getDeclaredMethods();
                for (Method method : declaredMethods) {
                    addHandlerExecutions(controller, method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        handlerExecutions.forEach((key, value) -> log.info("Path: {}, Controller: {}, Method: {}", key.getUrl(), value.getHandler(), key.getRequestMethod()));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void addHandlerExecutions(Map.Entry<Class<?>, Object> controller, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            for (RequestMethod requestMethod : requestMapping.method()) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMethod.name()));
                handlerExecutions.put(handlerKey, new HandlerExecution(controller.getValue(), method));
            }
        }
    }
}

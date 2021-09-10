package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(ControllerScanner controllerScanner) {
        this.controllerScanner = controllerScanner;
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> clazz = entry.getKey();
            Object controller = entry.getValue();
            findHandlerExecutions(clazz, controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void findHandlerExecutions(Class<?> clazz, Object controller) {
        for (Method method : clazz.getMethods()) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                break;
            }
            addHandlerExecutions(controller, method, requestMapping);
        }
    }

    private void addHandlerExecutions(Object controller, Method method, RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        for(RequestMethod requestMethod : methods){
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        return new HandlerKey(requestURI, requestMethod);
    }

}

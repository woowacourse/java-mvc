package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object obj : basePackage) {
            ControllerScanner controllerScanner = new ControllerScanner(obj);
            final Map<Class<?>, Object> controllers = controllerScanner.getController();
            final Set<Method> methods = controllerScanner.getMethods(controllers.keySet());
            initHandlerExecutions(methods, controllers);
        }
    }

    private void initHandlerExecutions(Set<Method> methods, Map<Class<?>, Object> controllers) {
        for (Method method : methods) {
            final List<HandlerKey> handlerKeys = getHandlerKey(method);
            handlerKeys.forEach(handlerKey -> {
                        final HandlerExecution handlerExecution = new HandlerExecution(
                                method,
                                controllers.get(method.getDeclaringClass())
                        );
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
            );
        }
    }

    private List<HandlerKey> getHandlerKey(Method method) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] methods = requestMapping.method();
        for (RequestMethod requestMethod : methods) {
            handlerKeys.add(new HandlerKey(requestMapping.value(), requestMethod));
        }
        return handlerKeys;
    }

    public Object getHandler(HttpServletRequest request) {
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);
        log.info("Requested HandlerKey : {}", handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}

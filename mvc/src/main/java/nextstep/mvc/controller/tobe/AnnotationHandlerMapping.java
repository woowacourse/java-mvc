package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        register(controllers);
    }

    private void register(Map<Class<?>, Object> controllers) {
        for (Class<?> controller : controllers.keySet()) {
            List<Method> methods = ReflectionUtils.getMethodsWithAnnotation(controller,
                    RequestMapping.class);
            for (Method method : methods) {
                setRequestMappingExecutions(controllers.get(controller), method);
            }
        }
    }

    private void setRequestMappingExecutions(Object clazz, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String value = annotation.value();
        RequestMethod[] method1 = annotation.method();
        for (RequestMethod requestMethod : method1) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, clazz);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method : {}", value, requestMethod);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String methodName = request.getMethod();
        RequestMethod requestMethod = RequestMethod.find(methodName);
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

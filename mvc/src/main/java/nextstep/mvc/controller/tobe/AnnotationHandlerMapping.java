package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.utils.AnnotationScanner;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
        this.controllerScanner = new ControllerScanner();
    }

    public void initialize() throws ReflectiveOperationException {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(this.basePackages);

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Set<Method> requestMappingMethods = AnnotationScanner.scanMethod(entry.getKey(), RequestMapping.class);
            registerHandlerExecution(entry.getValue(), requestMappingMethods);
        }
    }

    public void registerHandlerExecution(Object controller, Set<Method> requestMappingMethods) {
        for (Method method : requestMappingMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod),
                        new HandlerExecution(controller, method));
            }
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request)
    {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod().toUpperCase())));
    }
}

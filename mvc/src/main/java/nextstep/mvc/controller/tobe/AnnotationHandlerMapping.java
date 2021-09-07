package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.utils.AnnotationScanner;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws ReflectiveOperationException {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = AnnotationScanner.scanClassWith(this.basePackages, Controller.class);

        for (Class<?> controller : controllers) {
            Set<Method> requestMappingMethods = AnnotationScanner.scanMethod(controller, RequestMapping.class);
            registerHandlerExecution(controller, requestMappingMethods);
        }
    }

    public void registerHandlerExecution(Class<?> controller, Set<Method> requestMappingMethods) throws ReflectiveOperationException {
        Object object = controller.getDeclaredConstructor().newInstance();
        for (Method method : requestMappingMethods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod),
                        new HandlerExecution(object, method));
            }
        }
    }

    public Object getHandler(HttpServletRequest request)
    {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}

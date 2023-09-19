package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner();
        RequestMappingScanner requestMappingScanner = new RequestMappingScanner();

        Set<Class<?>> controllers = controllerScanner.scan(basePackage);
        Map<Class<?>, Object> instances = controllerScanner.createControllerInstance(controllers);

        List<Method> requestMappingMethods = requestMappingScanner.scanRequestMappingMethods(controllers);

        putHandlerExecutions(requestMappingMethods, instances);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutions(List<Method> requestMappingMethods, Map<Class<?>, Object> instances) {
        for (Method method : requestMappingMethods) {
            Object controller = instances.get(method.getDeclaringClass());
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String path = annotation.value();
            RequestMethod[] requestMethods = annotation.method();
            RequestMethod requestMethod = RequestMethod.valueOf(requestMethods[0].name());
            handlerExecutions.put(new HandlerKey(path, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        var controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for(Class controller:controllers){
            findMethodsWithController(controller);
        }
    }

    private void findMethodsWithController(Class controller) {
        var methods = controller.getDeclaredMethods();
        for(var method:methods){
            findAnnotationWithMethod(controller, method);
        }
    }

    private void findAnnotationWithMethod(Class controller, Method method) {
        if(!method.isAnnotationPresent(RequestMapping.class)){
            return;
        }
        var annotation = method.getDeclaredAnnotation(RequestMapping.class);
        for(RequestMethod requestMethod : annotation.method()){
            handlerExecutions.put(
                    new HandlerKey(annotation.value(),requestMethod),
                    new HandlerExecution(controller, method)
            );
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        request.getRequestURI();
        RequestMethod requestMethod = Enum.valueOf(RequestMethod.class,request.getMethod());
        HandlerKey key = new HandlerKey(request.getRequestURI(),requestMethod);
        return handlerExecutions.get(key);
    }
}

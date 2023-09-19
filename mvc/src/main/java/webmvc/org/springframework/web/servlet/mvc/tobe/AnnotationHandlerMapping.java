package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements Mapper{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllerClasses = getControllerClasses();
        for(Class<?> controllerClass : controllerClasses) {
            Object controller = findController(controllerClass);
            Method[] methods = controllerClass.getDeclaredMethods();
            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecution(controller, method));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> getControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Object findController(final Class<?> classWithAnnotation) {
        try{
            return classWithAnnotation.getConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("존재하지 않는 페이지 입니다.");
        }
    }

    private void addHandlerExecution(final Object controller, final Method method) {
        RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        String urlValue = annotation.value();
        for(RequestMethod requestMethod : annotation.method()){
            HandlerKey handlerKey = new HandlerKey(urlValue, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

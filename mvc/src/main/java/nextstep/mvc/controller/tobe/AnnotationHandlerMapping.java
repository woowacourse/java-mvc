package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.servlet.ControllerScanner;
import nextstep.mvc.servlet.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers(Controller.class);

        controllers.forEach(this::mapMethods);

        log.info("Initialized AnnotationHandlerMapping!");
        controllers.keySet().forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    private void mapMethods(Class<?> controller, Object instance) {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();
                RequestMethod[] requestMethods = requestMapping.method();
                mapHandlerExecutions(instance, method, url, requestMethods);
            }
        }
    }

    private void mapHandlerExecutions(Object newController, Method method, String url, RequestMethod[] requestMethods) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, newController);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        try {
            return handlerExecutions.get(handlerKey);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

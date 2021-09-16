package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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

        ControllerScanner controllerScanner = new ControllerScanner(this.basePackage);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        initController(controllers);
    }

    private void initController(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            requestMapping(controller);
        }
    }

    private void requestMapping(Entry<Class<?>, Object> controller) {
        Class<?> controllerClass = controller.getKey();

        Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class));

        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            RequestMethod[] requestMethods = requestMapping.method();

            for(RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);

                addHandlerExecution(handlerKey, controllerClass, method);
            }
        }
    }

    private void addHandlerExecution(HandlerKey handlerKey, Class<?> controllerClass, Method method) {
        try {
            Object handler = controllerClass.getConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

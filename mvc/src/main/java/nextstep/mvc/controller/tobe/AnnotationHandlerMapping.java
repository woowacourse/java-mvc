package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    private static final int HTTP_REQUEST_METHOD_INDEX = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        try {
            ControllerScanner controllerScanner = ControllerScanner.from(basePackage);
            Map<Class<?>, Object> controllers = controllerScanner.getControllers();
            initializeHandlerExecutions(controllers);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void initializeHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Entry<Class<?>, Object> controller : controllers.entrySet()) {
            Arrays.stream(controller.getKey().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> insertIntoHandlerExecutions(controller.getValue(), method));
        }
    }

    private void insertIntoHandlerExecutions(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(),
                requestMapping.method()[HTTP_REQUEST_METHOD_INDEX]);
        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        for (Class<?> controllerClass : controllerScanner.getControllers()) {
            initHandlerExecutions(controllerClass);
        }
        log.info("Initialized Annotation Handler Mapping!");
    }

    private void initHandlerExecutions(Class<?> controllerClass) {
        Object controllerInstance = constructInstance(controllerClass);
        Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class));
        for (Method method : methods) {
            putHandlerKeyByHandlerExecutor(controllerInstance, method);
        }
    }

    private Object constructInstance(Class<?> controller) {
        try {
            return controller.getConstructor()
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void putHandlerKeyByHandlerExecutor(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String value = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutions.put(new HandlerKey(value, requestMethod),
                    new HandlerExecution(controllerInstance, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.from(request));
    }
}

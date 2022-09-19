package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
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
        log.info("Initialized Annotation Handler Mapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClass : controllers) {
            initHandlerExecutions(getInstance(controllerClass), controllerClass.getMethods());
        }
    }

    private void initHandlerExecutions(Object controllerInstance, Method[] methods) {
        for (Method method : methods) {
            putHandlerKeyByHandlerExecutor(controllerInstance, method);
        }
    }

    private void putHandlerKeyByHandlerExecutor(Object controllerInstance, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        String value = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutions.put(new HandlerKey(value, requestMethod),
                    new HandlerExecution(controllerInstance, method));
        }
    }

    private Object getInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

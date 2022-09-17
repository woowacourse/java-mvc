package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (Object basePackage : basePackages){
            initializeHandlerExecution(basePackage);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerExecution(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            findRequestMapping(controller);
        }
    }

    private void findRequestMapping(Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            Object handler = createHandlerInstance(controller);
            addHandlerExecution(handler, method);
        }
    }

    private Object createHandlerInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private void addHandlerExecution(Object handler, Method declaredMethod) {
        if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
            handlerExecutions.put(
                new HandlerKey(requestMapping.value(), requestMapping.method()[0]),
                new HandlerExecution(handler, declaredMethod)
            );
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.from(request));
    }
}

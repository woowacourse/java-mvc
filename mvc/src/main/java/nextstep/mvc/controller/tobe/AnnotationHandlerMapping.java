package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        initControllers();
    }

    private void initControllers() {
        for (Object packageName : basePackage) {
            Reflections reflections = new Reflections(packageName);
            Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

            initMethods(controllers);
        }
    }

    private void initMethods(Set<Class<?>> controllers) {
        try {
            for (Class<?> controllerClass : controllers) {
                Object controllerInstance = controllerClass.getConstructor().newInstance();

                Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .forEach(it -> addHandlerExecutions(controllerInstance, it));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Method로부터 Controller를 찾을 수 없습니다.");
        }
    }

    private void addHandlerExecutions(Object controller, Method method) {
        RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        String path = annotation.value();

        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(path, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method, controller));

            log.info("Handler : {} {}", requestMethod.name(), path);
        }
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}

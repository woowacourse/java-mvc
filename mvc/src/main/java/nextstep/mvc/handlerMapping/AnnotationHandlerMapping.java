package nextstep.mvc.handlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class controller : controllers) {
            addAllToHandlerExecutions(controller);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void addAllToHandlerExecutions(final Class controller) {
        for (Method method : controller.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                addToHandlerExecutions(controller, method);
            }
        }
    }

    private void addToHandlerExecutions(final Class controller, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        List<HandlerKey> keys = Arrays.stream(annotation.method())
            .map(it -> new HandlerKey(annotation.value(), it))
            .collect(Collectors.toUnmodifiableList());

        HandlerExecution execution = new HandlerExecution(generateInstance(controller), method);

        for (final HandlerKey key : keys) {
            handlerExecutions.put(key, execution);
        }
    }

    private Object generateInstance(final Class controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException |
                 InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 생성 과정에서 예외가 발생했습니다.");
        }
    }

}

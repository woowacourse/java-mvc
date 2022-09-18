package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
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
            addToHandlerExecutions(controller);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }

    private void addToHandlerExecutions(final Class controller) {
        for (Method method : controller.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);

                HandlerKey key = new HandlerKey(annotation.value(), annotation.method()[0]);
                HandlerExecution execution = new HandlerExecution(generateInstance(controller), method);

                handlerExecutions.put(key, execution);
            }
        }
    }

    private Object generateInstance(Class controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException |
                 InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 생성 과정에서 예외가 발생했습니다.");
        }
    }

}

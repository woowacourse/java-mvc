package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

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
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));

        HandlerExecution cachedHandlerExecution = handlerExecutions.get(handlerKey);
        if (Objects.nonNull(cachedHandlerExecution)) {
            return cachedHandlerExecution;
        }

        return getHandlerExecution(handlerKey);
    }

    private HandlerExecution getHandlerExecution(HandlerKey handlerKey) {
        Reflections reflections = new Reflections(basePackage);

        for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            for (Method handlerMethod : controllerClass.getDeclaredMethods()) {
                if (!handlerMethod.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                RequestMapping annotation = handlerMethod.getAnnotation(RequestMapping.class);
                if (handlerKey.match(annotation)) {
                    Object instance = newInstance(controllerClass);
                    return getHandlerExecution(handlerKey, handlerMethod, instance);
                }
            }
        }
        return null;
    }

    private Object newInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            // todo
            log.error("컨트롤러 객체 생성 실패");
            throw new RuntimeException("컨트롤러 객체 생성 실패");
        }
    }

    private HandlerExecution getHandlerExecution(HandlerKey handlerKey, Method handlerMethod, Object instance) {
        handlerMethod.setAccessible(true);
        HandlerExecution handlerExecution = new HandlerExecution(handlerMethod, instance);
        handlerExecutions.put(handlerKey, handlerExecution);
        return handlerExecution;
    }
}

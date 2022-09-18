package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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
        Reflections reflections = new Reflections(basePackage);
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            addHandlerExecutions(aClass);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Class<?> aClass) {
        for (Method method : aClass.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                addHandlerExecution(aClass, method);
            }
        }
    }

    private void addHandlerExecution(final Class<?> aClass, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : annotation.method()) {
            try {
                Constructor<?> constructor = aClass.getConstructor();
                HandlerExecution handlerExecution = new HandlerExecution(constructor.newInstance(), method);
                handlerExecutions.put(new HandlerKey(annotation.value(), requestMethod), handlerExecution);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException exception) {
                log.warn(exception.getMessage());
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}

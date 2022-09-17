package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (var basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            extractClass(classes);
        }
    }

    private void extractClass(Set<Class<?>> classes) {
        for (var clazz : classes) {
            try {
                Object controller = clazz.getDeclaredConstructor().newInstance();
                extractMethodInClass(clazz, controller);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void extractMethodInClass(Class<?> clazz, Object controller) {
        for (var method : clazz.getMethods()) {
            extractTargetInMethod(controller, method);
        }
    }

    private void extractTargetInMethod(Object controller, Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            saveHandlerExecution(controller, method);
        }
    }

    private void saveHandlerExecution(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();
        for (RequestMethod requestMethod : methods) {
            log.info("SAVE >> url :{}, requestMethod:{}, method:{}", url, requestMethod, method);
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod httpMethod = RequestMethod.from(request.getMethod());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), httpMethod));
    }
}

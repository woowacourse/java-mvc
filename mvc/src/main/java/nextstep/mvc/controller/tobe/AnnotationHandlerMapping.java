package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Object basePackage : basePackages) {
            scanPackage(basePackage);
        }
        handlerExecutions.keySet()
            .forEach(handlerKey -> log.info("Path : {}, Controller : {}", handlerKey,
                handlerExecutions.get(handlerKey).getClass()));
    }

    private void scanPackage(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            scanController(controller);
        }
    }

    private void scanController(Class<?> controller) {
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            scanMethod(controller, method);
        }
    }

    private void scanMethod(Class<?> controller, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        if (requestMapping == null) {
            return;
        }

        String url = requestMapping.value();
        for (RequestMethod requestMethod : requestMapping.method()) {
            addHandler(controller, method, url, requestMethod);
        }
    }

    private void addHandler(Class<?> controller, Method method, String url, RequestMethod requestMethod) {
        HandlerKey key = new HandlerKey(url, requestMethod);

        try {
            Constructor<?> constructor = controller.getDeclaredConstructor();
            handlerExecutions.put(key, new HandlerExecution(constructor.newInstance(), method));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}

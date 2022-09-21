package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.*;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controllerClazz : controllerClasses) {
            handlerExecutions.putAll(toHandlerExecutions(controllerClazz));
        }
    }

    private Map<HandlerKey, HandlerExecution> toHandlerExecutions(final Class<?> controllerClazz) {
        return Arrays.stream(controllerClazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toMap(this::toHandlerKey, method -> new HandlerExecution(controllerClazz, method)));
    }

    private HandlerKey toHandlerKey(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}

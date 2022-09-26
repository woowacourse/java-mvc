package nextstep.mvc.handlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.mvc.exception.FailMapHandler;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = ControllerScanner.from(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Set<Class<?>> annotationControllers = controllerScanner.getControllers();
        for (Class<?> annotationController : annotationControllers) {
            mapRequestToMethod(annotationController);
        }
    }

    private void mapRequestToMethod(final Class<?> aClass) {
        final Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            putHandlerExecutions(method);
        }
    }

    private void putHandlerExecutions(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            final String url = requestMapping.value();
            final List<HandlerKey> handlerKeys = createHandlerKeys(requestMapping, url);
            for (HandlerKey handlerKey : handlerKeys) {
                final Object controller = mapHandler(method);
                handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
            }
        }
    }

    private List<HandlerKey> createHandlerKeys(final RequestMapping requestMapping, final String url) {
        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(toList());
    }

    private Object mapHandler(final Method method) {
        final Object handler;
        try {
            handler = method.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new FailMapHandler();
        }
        return handler;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}

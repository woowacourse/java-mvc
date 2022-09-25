package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
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
        ControllerScanner.getControllers(basePackage)
            .forEach(this::addHandlerExecutions);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        log.info("request handler [{}]", handlerKey);
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException(String.format("요청한 핸들러가 존재하지 않습니다. [%s]", handlerKey));
        }
        final HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        log.info("search handler [{}]", handlerExecution);
        return handlerExecution;
    }

    private void addHandlerExecutions(final Class<?> clazz, final Object controller) {
        final Method[] methods = clazz.getMethods();
        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .forEach(method -> addHandlerExecution(controller, method));
    }

    private void addHandlerExecution(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] requestMethods = requestMapping.method();
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        Arrays.stream(requestMethods)
            .forEach(requestMethod -> {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                putHandlerExecution(handlerExecution, handlerKey);
            });
    }

    private void putHandlerExecution(final HandlerExecution handlerExecution, final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException(String.format("중복적으로 매핑되었습니다. [%s]", handlerKey));
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }
}

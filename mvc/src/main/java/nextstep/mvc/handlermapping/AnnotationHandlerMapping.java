package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            addRequestMappingMethod(controller);
        }
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

    private void addRequestMappingMethod(final Class<?> controller) {
        final Method[] methods = controller.getMethods();
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecution(controller, requestMapping, method);
        }
    }

    private void addHandlerExecution(final Class<?> controller, final RequestMapping requestMapping, final Method method) {
        for (final RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalArgumentException(String.format("중복적으로 매핑되었습니다. [%s]", handlerKey));
            }
            handlerExecutions.put(handlerKey, getHandlerExecution(controller, method));
        }
    }

    private HandlerExecution getHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Object newInstance = controller.getConstructor().newInstance();
            return new HandlerExecution(newInstance, method);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("기본 생성자가 존재하지 않습니다. [%s]", controller));
        }
    }
}

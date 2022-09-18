package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        extractRequestMappings(classes).forEach(this::addHandler);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException(String.format("요청한 핸들러가 존재하지 않습니다. [%s]", handlerKey));
        }
        return handlerExecutions.get(handlerKey);
    }

    private List<RequestMapping> extractRequestMappings(final Set<Class<?>> classes) {
        final List<RequestMapping> requestMappings = new ArrayList<>();
        for (final Class<?> clazz : classes) {
            requestMappings.addAll(getRequestMappings(clazz));
        }
        return requestMappings;
    }

    private List<RequestMapping> getRequestMappings(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .map(method -> method.getAnnotation(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void addHandler(final RequestMapping requestMapping) {
        for (final RequestMethod method : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), method);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalArgumentException(String.format("중복적으로 매핑되었습니다. [%s]", handlerKey));
            }
            handlerExecutions.put(handlerKey, new HandlerExecution());
        }
    }
}

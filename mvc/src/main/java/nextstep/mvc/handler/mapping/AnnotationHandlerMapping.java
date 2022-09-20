package nextstep.mvc.handler.mapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.HandlerExecution;
import nextstep.mvc.handler.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        scanHandlersInBasePackages();
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void scanHandlersInBasePackages() {
        Reflections reflections = new Reflections(basePackages);
        reflections.getTypesAnnotatedWith(Controller.class)
            .forEach(this::scanRequestMappings);
    }

    private void scanRequestMappings(Class<?> controller) {
        for (Method method : controller.getMethods()) {
            HandlerExecution execution = new HandlerExecution(method);
            Optional.ofNullable(method.getAnnotation(RequestMapping.class))
                .ifPresent(requestMapping -> addHandlerExecution(requestMapping, execution));
        }
    }

    private void addHandlerExecution(RequestMapping requestMapping, HandlerExecution execution) {
        String url = requestMapping.value();
        for (RequestMethod method : requestMapping.method()) {
            handlerExecutions.put(new HandlerKey(url, method), execution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.fromRequest(request));
    }
}

package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.mvc.scanner.HandlerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final HandlerScanner handlerScanner = new HandlerScanner(basePackage);
        for (Map.Entry<Class<?>, Object> handlers : handlerScanner.getHandler().entrySet()) {
            for (Method declaredMethod : handlers.getKey().getDeclaredMethods()) {
                putHandlerExecutionByRequestMapping(handlers.getValue(), declaredMethod);
            }
        }
        handlerExecutions.forEach((key, value) -> log.info("Path: [{}], Controller: [{}], Method: [{}]", key.getUrl(), value.getHandler(), key.getRequestMethod()));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        log.debug("Request Annotation Mapping Uri : {}", request.getRequestURI());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private void putHandlerExecutionByRequestMapping(Object handler, Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMapping.method()[0].name()));
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }
}

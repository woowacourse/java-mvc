package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Map<HandlerKey, HandlerExecution> handlerExecutions = Objects.requireNonNull(HandlerExecutionFactory.create(basePackage));
        this.handlerExecutions.putAll(handlerExecutions);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey targetKey = new HandlerKey(requestURI, RequestMethod.from(method));

        if (handlerExecutions.containsKey(targetKey)) {
            return handlerExecutions.get(targetKey);
        }

        return null;
    }
}

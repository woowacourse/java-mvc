package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = addHandler(request, this.basePackage);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey addHandler(HttpServletRequest request, Object[] basePackage) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String url = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        if (!handlerExecutions.containsKey(handlerKey)) {
            handlerExecutions.put(handlerKey, new HandlerExecution(basePackage));
        }
        return handlerKey;
    }
}

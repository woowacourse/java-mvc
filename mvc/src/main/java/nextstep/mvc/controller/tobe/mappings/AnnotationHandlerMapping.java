package nextstep.mvc.controller.tobe.mappings;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        HandlerExecutionsFinder finder = new HandlerExecutionsFinder();
        for (String basePackage : basePackages) {
            handlerExecutions.putAll(finder.findHandlerExecutions(basePackage));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = Arrays.stream(RequestMethod.values())
                .filter(method -> method.name().equalsIgnoreCase(request.getMethod()))
                .findAny()
                .orElseThrow();
        String requestURI = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

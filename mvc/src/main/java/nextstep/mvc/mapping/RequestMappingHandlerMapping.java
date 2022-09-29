package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMappingHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(RequestMappingHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerMappings;

    public RequestMappingHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerMappings = new HashMap<>();
    }

    @Override
    public void initialize() {
        handlerMappings.putAll(new ControllerScanner(basePackage).scan());

        log.info("Initialized AnnotationHandlerMapping!");
        handlerMappings.forEach((key, value) -> log.info("Path : {}, Controller : {}", key, value));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final var requestURI = request.getRequestURI();
        final var requestMethod = RequestMethod.from(request.getMethod());
        final var handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerMappings.get(handlerKey);
    }
}

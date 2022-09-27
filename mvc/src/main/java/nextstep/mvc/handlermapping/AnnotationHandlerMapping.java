package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final AnnotationHandlerScanner annotationHandlerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.annotationHandlerScanner = new AnnotationHandlerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        this.handlerExecutions.putAll(annotationHandlerScanner.scan());
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        try {
            HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
            return handlerExecutions.get(handlerKey);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("[ERROR] HandlerExecution 이 존재하지 않습니다.");
        }
    }
}

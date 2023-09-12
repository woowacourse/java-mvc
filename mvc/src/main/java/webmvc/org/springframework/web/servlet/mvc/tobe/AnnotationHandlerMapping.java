package webmvc.org.springframework.web.servlet.mvc.tobe;

import static web.org.springframework.web.bind.annotation.RequestMethod.GET;
import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.put(new HandlerKey("/get-test", GET), new HandlerExecution());
        handlerExecutions.put(new HandlerKey("/post-test", POST), new HandlerExecution());
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(method)));
    }
}

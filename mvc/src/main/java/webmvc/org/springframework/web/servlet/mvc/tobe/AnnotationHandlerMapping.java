package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.ComponentScanner;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ComponentScanner componentScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(ComponentScanner componentScanner) {
        this.componentScanner = componentScanner;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final var methods = componentScanner.getMethodsAnnotateWithFromTypes(Controller.class, RequestMapping.class);
        methods.forEach(this::addHandlerExecution);
        log.info("Initialized AnnotationHandlerMapping!");
    }


    private void addHandlerExecution(Object controller, Method method) {
        final var requestMapping = method.getAnnotation(RequestMapping.class);
        final var mappingUrl = requestMapping.value();
        final var mappingRequestMethods = requestMapping.method();

        for (RequestMethod requestMethod : mappingRequestMethods) {
            handlerExecutions.put(new HandlerKey(mappingUrl, requestMethod), new HandlerExecution(controller, method));
        }
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final var key = new HandlerKey(request.getRequestURI(), RequestMethod.find(request.getMethod()));

        return handlerExecutions.get(key);
    }
}

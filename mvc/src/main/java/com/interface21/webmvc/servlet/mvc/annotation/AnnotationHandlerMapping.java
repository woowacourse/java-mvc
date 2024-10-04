package com.interface21.webmvc.servlet.mvc.annotation;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.HandlerKey;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private static final int EMPTY_REQUEST_METHODS = 0;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();
        controllers.values().forEach(this::addMapperOfController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addMapperOfController(Object controller) {
        Method[] handlers = controller.getClass().getDeclaredMethods();
        Arrays.stream(handlers)
                .filter(handler -> handler.isAnnotationPresent(RequestMapping.class))
                .forEach(handler -> addMapper(controller, handler));
    }

    private void addMapper(Object controller, Method handler) {
        List<HandlerKey> handlerKeys = createHandlerKeys(handler.getAnnotation(RequestMapping.class));
        HandlerExecution handlerExecution = new HandlerExecution(controller, handler);
        handlerKeys.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    private List<HandlerKey> createHandlerKeys(RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = decideRequestMethods(requestMapping);
        return Arrays.stream(requestMethods)
                .map(requestMethod -> HandlerKey.from(uri, requestMethod))
                .toList();
    }

    private RequestMethod[] decideRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == EMPTY_REQUEST_METHODS) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = createHandlerKey(request);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException(
                    String.format("해당 요청에 대응하는 핸들러가 없습니다: %s %s", request.getMethod(), request.getRequestURI()));
        }
        return handlerExecution;
    }

    private HandlerKey createHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod());
        return HandlerKey.from(requestURI, requestMethod);
    }
}

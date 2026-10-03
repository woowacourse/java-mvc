package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.bind.annotation.UnknownHttpMethodException;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerScanner controllerScanner;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        controllerScanner = new ControllerScanner(basePackage);
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        controllerScanner.getControllers()
                .forEach(this::enrollController);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod;
        try {
            requestMethod = RequestMethod.getRequestMethod(request.getMethod());
        } catch (UnknownHttpMethodException e) {
            return null;
        }

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public Set<RequestMethod> getAllowedMethods(final String requestURI) {
        Set<RequestMethod> allowedMethods = EnumSet.noneOf(RequestMethod.class);
        for (HandlerKey handlerKey : handlerExecutions.keySet()) {
            if (handlerKey.getUrl().equals(requestURI)) {
                allowedMethods.add(handlerKey.getRequestMethod());
            }
        }
        return allowedMethods;
    }

    private void enrollController(Class<?> aClass, Object controller) {
        for (Method method : aClass.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                enrollHandler(requestMapping, method, controller);
            }
        }
    }

    private void enrollHandler(RequestMapping requestMapping, Method method, Object controller) {
        validateReturnType(method);

        String uri = requestMapping.value();

        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution execution = (request, response) ->
                    (ModelAndView) method.invoke(controller, request, response);

            put(handlerKey, execution);
        }
    }

    @Nonnull
    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private void put(HandlerKey handlerKey, HandlerExecution handlerExecution) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("이미 등록된 HandlerKey입니다. " + handlerKey.toString());
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private void validateReturnType(Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException(
                    "Handler의 반환타입이 ModelAndView가 아닙니다. type: " + method.getReturnType().getName());
        }
    }
}

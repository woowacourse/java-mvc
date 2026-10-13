package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private static final Class<?>[] HANDLER_PARAMETER_TYPES = {HttpServletRequest.class, HttpServletResponse.class};

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            for (Method method : controller.getKey().getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    register(method, controller.getValue());
                }
            }
            log.info("찾은 컨트롤러: {}", controller.getKey().getName());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void register(Method method, Object instance) {
        validateHandlerMethod(method);
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMethod.name()));
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalStateException("중복된 매핑이 존재합니다. : " + key);
            }
            handlerExecutions.put(key, new HandlerExecution(instance, method));
            log.info("등록: {}", key);
        }
    }

    private void validateHandlerMethod(final Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException("핸들러 메서드의 반환 타입은 ModelAndView여야 합니다. : " + method);
        }
        if (!Arrays.equals(method.getParameterTypes(), HANDLER_PARAMETER_TYPES)) {
            throw new IllegalStateException(
                    "핸들러 메서드의 매개변수는 (HttpServletRequest, HttpServletResponse)여야 합니다. : " + method);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = String.valueOf(request.getRequestURI());
        RequestMethod requestMethod = findRequestMethod(request.getMethod());
        if (requestMethod == null) {
            return null;
        }
        HandlerKey key = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(key);
    }

    private RequestMethod findRequestMethod(final String method) {
        try {
            return RequestMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

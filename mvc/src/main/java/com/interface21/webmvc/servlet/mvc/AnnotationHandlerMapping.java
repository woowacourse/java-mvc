package com.interface21.webmvc.servlet.mvc;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.web.util.UrlPathHelper;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<?>[] HANDLER_PARAMETER_TYPES = {HttpServletRequest.class, HttpServletResponse.class};

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        new ControllerScanner(basePackage).getControllers().forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach((handlerKey, handlerExecution) ->
                log.info("{} -> {}", handlerKey, handlerExecution));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String lookupPath = UrlPathHelper.getPathWithinApplication(request);
        return RequestMethod.findByName(request.getMethod())
                .map(requestMethod ->
                        handlerExecutions.get(new HandlerKey(lookupPath, requestMethod)))
                .orElse(null);
    }

    private void registerController(final Class<?> controller, final Object instance) {
        for (final Method method : ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class))) {
            registerHandler(method, instance);
        }
    }

    private void registerHandler(final Method method, final Object instance) {
        validateHandlerMethod(method);
        final var annotation = method.getAnnotation(RequestMapping.class);
        final var handlerExecution = new HandlerExecution(instance, method);
        for (final RequestMethod requestMethod : getRequestMethods(annotation)) {
            addHandlerExecution(new HandlerKey(annotation.value(), requestMethod), handlerExecution);
        }
    }

    private void validateHandlerMethod(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())
                || !Arrays.equals(method.getParameterTypes(), HANDLER_PARAMETER_TYPES)
                || method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException("핸들러 메서드가 지원하지 않는 형식입니다: " + method);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping annotation) {
        if (annotation.method().length == 0) {
            return RequestMethod.values();
        }
        return annotation.method();
    }

    private void addHandlerExecution(final HandlerKey handlerKey, final HandlerExecution handlerExecution) {
        final HandlerExecution existing = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
        if (existing != null) {
            throw new IllegalStateException(
                    "중복된 요청 매핑입니다: " + handlerKey + " (" + existing + ", " + handlerExecution + ")");
        }
    }
}

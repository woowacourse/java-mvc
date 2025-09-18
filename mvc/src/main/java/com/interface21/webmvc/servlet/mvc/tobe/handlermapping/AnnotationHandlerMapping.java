package com.interface21.webmvc.servlet.mvc.tobe.handlermapping;

import com.interface21.core.util.AnnotationScanner;
import com.interface21.core.util.ControllerScanner;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKeyFactory;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Map<Class<?>, Object> controllers = ControllerScanner.scanControllers(basePackage);
        for (final Class<?> controller : controllers.keySet()) {
            final List<Method> handlerMethods = AnnotationScanner.scanMethods(controller, RequestMapping.class);
            scanHandlerMethods(controllers.get(controller), handlerMethods);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException(
                    String.format("%s %s 요청에 대한 핸들러를 찾을 수 없습니다.", request.getRequestURI(), request.getMethod()));
        }
        return handlerExecutions.get(handlerKey);
    }

    private void scanHandlerMethods(final Object controller, final List<Method> methods) {
        for (final Method method : methods) {
            final RequestMapping requestMapping = Objects.requireNonNull(
                    method.getDeclaredAnnotation(RequestMapping.class));
            final List<HandlerKey> keys = HandlerKeyFactory.from(requestMapping);
            for (final HandlerKey key : keys) {
                final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                handlerExecutions.put(key, handlerExecution);
                log.info("HandlerKey : {}, HandlerExecution : {}", key, handlerExecution);
            }
        }
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.AnnotationScanner;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        final Set<Class<?>> controllers = AnnotationScanner.scanClassesOfBasePackage(Controller.class, basePackage);
        for (final Class<?> controller : controllers) {
            final List<Method> handlerMethods = AnnotationScanner.scanMethods(controller, RequestMapping.class);
            scanHandlerMethods(controller, handlerMethods);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private void scanHandlerMethods(final Class<?> controller, final List<Method> methods) {
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            final List<HandlerKey> keys = HandlerKeyFactory.from(requestMapping);
            for (final HandlerKey key : keys) {
                handlerExecutions.put(key, createHandlerExecution(controller, method));
            }
        }
    }

    private HandlerExecution createHandlerExecution(final Class<?> controller, final Method method) {
        try {
            final Object controllerInstance = controller.getDeclaredConstructor().newInstance();
            return new HandlerExecution(controllerInstance, method);
        } catch (Exception e) {
            throw new IllegalStateException("controller 인스턴스 생성 실패"); // TODO : 에러 핸들러 추가하기
        }
    }
}

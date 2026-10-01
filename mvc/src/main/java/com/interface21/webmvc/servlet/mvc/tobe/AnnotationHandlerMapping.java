package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.forEach((controllerClass, controller) -> {
            for (Method method : controllerClass.getMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {   // ← 이거
                    continue;
                }
                registerHandler(controller, method);
            }
        });

        log.info("Initialized AnnotationHandlerMapping!");
    }


    private void registerHandler(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();

        checkIfControllerMethodHasValidSignature(method);
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            registerHandlerByCriteria(controller, method, key);
        }
    }

    private void registerHandlerByCriteria(Object controller, Method method, HandlerKey key) {
        if (!handlerExecutions.containsKey(key)) {
            handlerExecutions.put(key, new HandlerExecution(controller, method));
            return;
        }

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = handlerExecutions.get(key);
        if (requestMapping.method().length > 0 && !handlerExecution.containsExplicitMethod()) {
            // http method가 생략되어 있으면 명시된 것 우선
            handlerExecutions.put(key, new HandlerExecution(controller, method));
            return;
        }
        if (requestMapping.method().length == 0 && handlerExecution.containsExplicitMethod()) {
            return;
        }

        throw new IllegalStateException("이미 등록된 핸들러입니다.");
    }

    private void checkIfControllerMethodHasValidSignature(Method method) {
        List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
        if (!hasValidParametersForRequestMapping(parameterTypes)) {
            throw new IllegalStateException("HttpServletRequest와 HttpServletResponse 타입의 파라미터가 필요합니다.");
        }
        if (!ModelAndView.class.equals(method.getReturnType())) {
            throw new IllegalStateException("반환 타입이 ModelAndView가 아닙니다.");
        }
    }

    private static boolean hasValidParametersForRequestMapping(List<Class<?>> parameterTypes) {
        if (parameterTypes.size() != 2) {
            return false;
        }

        boolean hasCorrectOrder = parameterTypes.get(0).equals(HttpServletRequest.class)
                && parameterTypes.get(1).equals(HttpServletResponse.class);

        return hasCorrectOrder
                && parameterTypes.contains(HttpServletRequest.class)
                && parameterTypes.contains(HttpServletResponse.class);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        try {
            HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.resolve(request.getMethod()));
            return handlerExecutions.get(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Class<?> controllerClass : controllers.keySet()) {
            Object controller = controllers.get(controllerClass);

            // @RequestMapping이 붙은 메서드를 찾아 등록한다.
            Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : methods) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                registerHandlerExecution(method, controller, mapping);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecution(Method method, Object controller, RequestMapping mapping) {
        HandlerExecution execution = new HandlerExecution(controller, method);
        RequestMethod[] requestMethods = resolveRequestMethods(mapping);

        // URL과 HTTP 메서드를 키로 실행 정보를 등록한다.
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mapping.value(), requestMethod);
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalStateException("중복된 요청 매핑: " + key);
            }
            handlerExecutions.put(key, execution);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping mapping) {
        RequestMethod[] requestMethods = mapping.method();
        // RequestMapping에 메서드가 지정되어있지 않다면 모든 메서드를 지원한다.
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        HandlerKey key = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(key);
    }
}

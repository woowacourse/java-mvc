package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        ControllerScanner scanner = new ControllerScanner();
        Map<Class<?>, Object> controllers = scanner.getControllerInstances(basePackage);
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerType = entry.getKey();
            Object controllerInstance = entry.getValue();

            Set<Method> requestMethods = scanner.getRequestMappingMethods(controllerType);

            for (Method method : requestMethods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();

                for (RequestMethod requestMethod : requestMapping.method()) {
                    HandlerKey key = new HandlerKey(url, requestMethod);
                    HandlerExecution execution = new HandlerExecution(controllerInstance, method);
                    handlerExecutions.put(key, execution);
                    log.info("Mapped {} : {}", key, execution);
                }
            }
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}

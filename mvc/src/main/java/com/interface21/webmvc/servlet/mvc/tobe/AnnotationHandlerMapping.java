package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> controllerClass = entry.getKey();
            Object controller = entry.getValue();
            register(controllerClass, controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void register(Class<?> controllerClass, Object controller) {
        final Set<Method> methods = ReflectionUtils.getAllMethods(
                controllerClass,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        for (Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            registerMethod(method, requestMapping, controller);
        }
    }

    private void registerMethod(Method method, RequestMapping requestMapping, Object controller) {
        RequestMethod[] requestMethods = requestMapping.method();
        String url = requestMapping.value();

        requestMethods = getAllRequestMethodsIfAbsent(requestMethods);

        for (RequestMethod rm : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, rm);
            addHandlerExecution(method, handlerKey, controller);
        }
    }

    private RequestMethod[] getAllRequestMethodsIfAbsent(RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private void addHandlerExecution(Method method, HandlerKey handlerKey,  Object controller) {
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            HandlerExecution previous = handlerExecutions.putIfAbsent(handlerKey, handlerExecution);

            if (previous != null) {
                throw new IllegalStateException(
                        "초기화 실패 : 중복 매핑 존재" + handlerKey
                                + ", 기존 핸들러: " + previous
                                + ", 신규 핸들러: " + handlerExecution);
            }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        final String servletPath = request.getServletPath();
        return handlerExecutions.get(new HandlerKey(servletPath, requestMethod));
    }
}

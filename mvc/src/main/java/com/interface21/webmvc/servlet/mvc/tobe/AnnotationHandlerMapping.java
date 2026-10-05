package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            register(controllerClass);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void register(Class<?> controllerClass) {
        final Object controller = createController(controllerClass);

        for (Method method : controllerClass.getDeclaredMethods()) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

            if (requestMapping == null) {
                continue;
            }

            registerMethod(method, requestMapping, controller);
        }
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("초기화 실패 : 컨트롤러 생성 실패", e);
        }
    }

    private void registerMethod(Method method, RequestMapping requestMapping, Object controller) {
        RequestMethod[] requestMethods = requestMapping.method();
        String url = requestMapping.value();

        requestMethods = getAllRequestMethodsIfAbsent(requestMethods);
        addHandlerExecution(method, requestMethods, url, controller);
    }

    private RequestMethod[] getAllRequestMethodsIfAbsent(RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private void addHandlerExecution(Method method, RequestMethod[] requestMethods, String url, Object controller) {
        for (RequestMethod rm : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, rm);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            HandlerExecution previous =
                    handlerExecutions.putIfAbsent(handlerKey, handlerExecution);

            if (previous != null) {
                throw new IllegalStateException(
                        "초기화 실패 : 중복 매핑 존재" + handlerKey
                                + ", 기존 핸들러: " + previous
                                + ", 신규 핸들러: " + handlerExecution);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        final String servletPath = request.getServletPath();
        return handlerExecutions.get(new HandlerKey(servletPath, requestMethod));
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

        final Set<Class<?>> controllers = findControllers();
        for (Class<?> controller : controllers) {
            findControllerMethods(controller);
        }
    }

    // 1. basePackage에서 @Controller 붙은 클래스들 찾기
    private Set<Class<?>> findControllers() {
        final Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    // 2. 각 컨트롤러 클래스에서 Method 별로 @RequestMapping 찾아서 Handler에 등록
    private void findControllerMethods(final Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(controller, method);
            }
        }
    }

    private void registerHandler(final Class<?> controller, final Method method) {
        // 3. @RequestMapping에서 uri, requestMethod 추출
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String uri = requestMapping.value();
        final RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        try {
            // 6. requestMethod 마다 HandlerKey, HandlerExecution 생성 후 등록
            final Object controllerInstance = controller.getConstructor().newInstance();
            for (RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
                final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

                if (handlerExecutions.containsKey(handlerKey)) {
                    throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
                }
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info("mapped: {} {}", requestMethod, uri);
            }
        } catch (Exception e) {
            log.warn("initialize exception", e);
            throw new RuntimeException(e);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        // 4. requestMethod는 여러 개 가능하나,
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            // 5. 메서드에 requestMethod가 없다면 모든 HTTP method를 지원한다.
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());

        final HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }
}

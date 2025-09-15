package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 애노테이션 기반 핸들러 매핑 관리 - @Controller가 붙은 클래스 스캔 - @RequestMapping이 붙은 메서드 추출 - Key: URL + HTTP Method -> Value:
 * HandlerExecution 매핑
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllers = scanControllers();
        registerHandlers(controllers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod())));
    }

    // basePackage에서 @Controller가 붙은 클래스 스캔
    private Set<Class<?>> scanControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    // 컨트롤러 클래스에서 @RequestMapping 메서드를 찾아 HandlerExecution 등록
    private void registerHandlers(Set<Class<?>> controllers) {
        // 뎁스가 깊긴한데.. 메서드 분리나 스트림 쓰는거보다 가독성 더 좋은 듯
        for (Class<?> controller : controllers) {
            for (Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    registerHandler(controller, method);
                }
            }
        }
    }

    // 단일 컨트롤러 메서드 등록
    private void registerHandler(Class<?> controller, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        String path = mapping.value();
        RequestMethod[] requestMethods = mapping.method();

        requestMethods = resolveRequestMethods(requestMethods);
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(path, requestMethod);
            validateDuplicatedKey(key);
            HandlerExecution execution = new HandlerExecution(controller, method);
            handlerExecutions.put(key, execution);
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMethod[] requestMethods) {
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }

    private void validateDuplicatedKey(HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalStateException("Handler Key already exists :" + key);
        }
    }
}

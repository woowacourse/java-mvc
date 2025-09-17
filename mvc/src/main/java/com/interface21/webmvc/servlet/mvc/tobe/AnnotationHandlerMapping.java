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

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    // 초기화 메서드
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackages);

        Set<Class<?>> controllerClasses = findControllerClasses(reflections);
        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }
    }

    // 컨트롤러 클래스 조회
    private Set<Class<?>> findControllerClasses(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    // 컨트롤러 등록
    private void registerController(Class<?> controllerClass) {
        Object controller = createInstance(controllerClass);
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandlerMethod(controller, method);
            }
        }
    }

    // 핸들러 메서드 등록
    private void registerHandlerMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values(); // 모든 메서드 허용
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.putIfAbsent(handlerKey, handlerExecution);
            log.debug("Handler registered: {} {}", requestMethod, uri);
        }
    }

    // 컨트롤러 인스턴스 생성
    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.info("인스턴스 생성에 실패했습니다. {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // 요청으로부터 핸들러 조회
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}


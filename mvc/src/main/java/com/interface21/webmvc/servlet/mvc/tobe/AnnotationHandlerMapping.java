package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        // Controller 탐색/생성은 ControllerScanner에게 맡긴다.
        final Map<Class<?>, Object> controllers = controllerScanner.scan();

        // 각 Controller의 @RequestMapping 정보를 Handler로 등록한다.
        controllers.forEach(this::registerHandlerExecutions);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerExecutions(final Class<?> controllerClass, final Object controller) {
        // Controller에 선언된 메서드를 확인한다.
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;// @RequestMapping이 없는 메서드는 Handler가 아니다.
            }

            registerHandler(controller, method);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        // @RequestMapping에서 URL과 HTTP Method 조건을 읽는다.
        //@RequestMapping(value = "/get-test", method = RequestMethod.GET) 자체를 객체로 가져옴
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        final RequestMethod[] requestMethods = requestMapping.method();

        // method를 지정하지 않았다면 모든 HTTP Method를 지원한다.
        if (requestMethods.length == 0) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                registerHandler(requestMapping.value(), requestMethod, controller, method);
            }
            return;
        }

        for (RequestMethod requestMethod : requestMethods) {
            registerHandler(requestMapping.value(), requestMethod, controller, method);
        }
    }

    private void registerHandler(
            final String url,
            final RequestMethod requestMethod,
            final Object controller,
            final Method method
    ) {
        // URL + HTTP Method를 Handler를 찾기 위한 Key로 사용한다.
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        // 실제로 실행할 Controller 객체와 Method를 함께 보관한다.
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        handlerExecutions.put(handlerKey, handlerExecution);
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        // 실제 요청 역시 URL + HTTP Method로 Handler를 조회한다.
        final String url = request.getRequestURI();

        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
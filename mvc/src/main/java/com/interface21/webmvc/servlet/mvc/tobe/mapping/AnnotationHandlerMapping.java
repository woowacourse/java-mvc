package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import java.util.Arrays;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 어떤 URL로 요청이 들어왔을 대, 어떤 컨트롤러의 어떤 메서드를 실행할지 결정하는 클래스
 */
public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    //@Controller를 찾기 위해 탐색 시작할 패키지
    private final Object[] basePackages;
    //HandlerKey: 클라이언트의 요청을 어떤 컨트롤러 메서드가 처리할지 결정하는 정보를 담음 (요청 URL + HTTP Method)
    //HandlerExecution: 실제로 요청을 처리할 컨트롤러 메서드를 실행하는 과정.
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            final Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackages);
            initializeHandlerExecutions(controllers);
        } catch (Exception e) {
            throw new RuntimeException("AnnotationHandlerMapping 초기화에 실패했습니다.", e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    /**
     * 각 컨트롤러의 메서드를 스캔하고 @RequestMapping 정보를 기반으로 핸들러 맵을 초기화
     */
    private void initializeHandlerExecutions(final Map<Class<?>, Object> controllers) {
        controllers.forEach((clazz, controller) ->
                Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> registerHandlerExecution(method, method.getAnnotation(RequestMapping.class), controller))
        );
    }

    /**
     * 핸들러 메서드 정보를 handlerExecutions 맵에 등록
     */
    private void registerHandlerExecution(final Method method, final RequestMapping requestMapping, final Object controller) {
        final String url = requestMapping.value();
        RequestMethod[] targetMethods = requestMapping.method();
        if (targetMethods.length == 0) {
            targetMethods = new RequestMethod[]{RequestMethod.GET};
        }

        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (final RequestMethod requestMethod : targetMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("Duplicate mapping found: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}

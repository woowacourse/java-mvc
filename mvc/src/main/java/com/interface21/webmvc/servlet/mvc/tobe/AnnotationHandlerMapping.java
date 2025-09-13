package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 어떤 URL로 요청이 들어왔을 대, 어떤 컨트롤러의 어떤 메서드를 실행할지 결정하는 클래스
 */
public class AnnotationHandlerMapping {

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
            for (Object basePackage : basePackages) {
                Reflections reflections = new Reflections(basePackage);
                // @Controller 어노테이션이 붙은 클래스들 가져오기
                final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
                final Map<Class<?>, Object> controllers = createControllerInstances(controllerClasses);
                initializeHandlerExecutions(controllers);
            }
        } catch (Exception e) {
            throw new RuntimeException("AnnotationHandlerMapping 초기화에 실패했습니다.", e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    /**
     * @Controller 어노테이션이 붙은 클래스들의 인스턴스를 생성하여 맵 형태로 반환
     */
    private Map<Class<?>, Object> createControllerInstances(final Set<Class<?>> controllerClasses) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClass : controllerClasses) {
            controllers.put(controllerClass, controllerClass.getDeclaredConstructor().newInstance());
        }
        return controllers;
    }

    /**
     * 각 컨트롤러의 메서드를 스캔하고 @RequestMapping 정보를 기반으로 핸들러 맵을 초기화
     */
    private void initializeHandlerExecutions(final Map<Class<?>, Object> controllers) {
        for (final Class<?> controllerClass : controllers.keySet()) {
            final Object controller = controllers.get(controllerClass);
            final Method[] methods = controllerClass.getDeclaredMethods();
            for (final Method method : methods) {
                //메서드에 붙은 @RequestMapping 어노테이션 가져오기
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                // @RequestMapping이 안붙은 메서드이면 null
                if (requestMapping != null) {
                    registerHandlerExecution(method, requestMapping.method(), requestMapping.value(), controller);
                }
            }
        }
    }

    /**
     * 핸들러 메서드 정보를 handlerExecutions 맵에 등록
     */
    private void registerHandlerExecution(final Method method, final RequestMethod[] requestMethods, final String url, final Object controller) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("이미 존재하는 HandlerKey 입니다.");
            }
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }
}

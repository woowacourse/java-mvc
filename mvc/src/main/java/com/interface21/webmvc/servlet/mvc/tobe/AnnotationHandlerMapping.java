package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
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
            final Map<Class<?>, Object> controllers = initializeControllerInstances();
            final Map<HandlerKey, HandlerExecution> executions = buildHandlerExecutions(controllers);
            handlerExecutions.putAll(executions);
        } catch (Exception e) {
            throw new RuntimeException("AnnotationHandlerMapping 초기화에 실패했습니다.", e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private Map<Class<?>, Object> initializeControllerInstances() throws Exception {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Object basePackage : basePackages) {
            final Set<Class<?>> controllerClasses = scanControllerClasses(basePackage);
            final Map<Class<?>, Object> controllerInstances = createControllerInstances(controllerClasses);
            controllers.putAll(controllerInstances);
        }
        return controllers;
    }

    private Set<Class<?>> scanControllerClasses(Object basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    /**
     * @Controller 어노테이션이 붙은 클래스들의 인스턴스를 생성하여 맵 형태로 반환
     */
    private Map<Class<?>, Object> createControllerInstances(final Set<Class<?>> controllerClasses) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> controllerClass : controllerClasses) {
            Constructor<?> constructor = controllerClass.getDeclaredConstructor();
            controllers.put(controllerClass, constructor.newInstance());
        }
        return controllers;
    }

    /**
     * 각 컨트롤러의 메서드를 스캔하고 @RequestMapping 정보를 기반으로 핸들러 맵을 구성
     */
    private Map<HandlerKey, HandlerExecution> buildHandlerExecutions(final Map<Class<?>, Object> controllers) {
        final Map<HandlerKey, HandlerExecution> executions = new HashMap<>();
        for (final Class<?> controllerClass : controllers.keySet()) {
            final Object controller = controllers.get(controllerClass);
            processControllerMethods(executions, controllerClass, controller);
        }
        return executions;
    }

    private void processControllerMethods(final Map<HandlerKey, HandlerExecution> executions, final Class<?> controllerClass, final Object controller) {
        final Method[] methods = controllerClass.getDeclaredMethods();
        for (final Method method : methods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                registerHandlerExecution(executions, requestMapping, handlerExecution);
            }
        }
    }

    /**
     * 핸들러 메서드 정보를 handlerExecutions 맵에 등록
     */
    private void registerHandlerExecution(
            final Map<HandlerKey, HandlerExecution> handlerExecutionMap,
            final RequestMapping requestMapping,
            final HandlerExecution handlerExecution
    ) {
        final String url = requestMapping.value();
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            if (handlerExecutionMap.containsKey(handlerKey)) {
                throw new IllegalStateException("이미 존재하는 HandlerKey 입니다.");
            }
            handlerExecutionMap.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = new RequestMethod[]{RequestMethod.GET};
        }
        return requestMethods;
    }
}

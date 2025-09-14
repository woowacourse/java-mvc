package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * HTTP 요청을 처리할 컨트롤러 메서드를 등록하고 찾아주는 매핑 클래스.
 *
 * - URL + HTTP Method 조합을 기준으로 요청을 컨트롤러 메서드에 매핑한다.
 * - 스캔 대상은 @Controller 애노테이션이 붙은 클래스만 해당된다.
 * - 매핑된 컨트롤러 메서드는 이후 Dispatcher 역할을 하는 컴포넌트가 호출하여 실제 요청을 처리한다.
 */
public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage; // 내가 만든 컨트롤러들이 들어있는 패키지 위치
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    /**
     * 컨트롤러 클래스들을 스캔해서 매핑을 등록하는 핵심 로직
     */
    public void initialize() {
        registerControllers(scanControllers());
        log.info("Initialized AnnotationHandlerMapping! size={}", handlerExecutions.size());
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }

    private void registerControllers(Set<Class<?>> controllers) {
        controllers.forEach(this::registerController);
    }

    private Set<Class<?>> scanControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            for (Method method : controllerClass.getDeclaredMethods()) {
                registerHandlerMethod(controllerInstance, method);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize controller: " + controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }

        for (RequestMethod requestMethod : resolveRequestMethods(mapping)) {
            HandlerKey handlerKey = new HandlerKey(getPath(mapping), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("중복 매핑 발견: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
        }
    }

    private String getPath(RequestMapping mapping) {
        String path = mapping.value();
        if (path.isBlank()) {
            return "/";
        }
        return mapping.value();
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping mapping) {
        RequestMethod[] methods = mapping.method();
        if (methods.length == 0) {
            // 모든 HTTP Method(GET, POST ...) 허용
            methods = RequestMethod.values();
        }
        return methods;
    }
}

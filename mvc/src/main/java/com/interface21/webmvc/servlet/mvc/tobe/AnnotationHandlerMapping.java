package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.asis.ControllerScanner;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> methodSet = getRequestMappingMethods(controllers.keySet());
        for (Method method : methodSet) {
            // RequestMapping 어노테이션에 사용된 값 객체 생성
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, method));
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        Class<?> controllerClass = method.getDeclaringClass();
        Object controller = controllers.get(controllerClass);
        HandlerExecution execution = new HandlerExecution(controller, method);
        List<HandlerKey> keys = mapHandlerKeys(
                requestMapping.value(),
                getRequestMethods(requestMapping)
        );

        for (HandlerKey key : keys) {
            // 이미 같은 URL과 Method 조합이 존재하는지 검사
            putHandlerExecution(key, execution);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(url, requestMethod);
            handlerKeys.add(key);
        }
        return handlerKeys;
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(type -> Arrays.stream(type.getDeclaredMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void putHandlerExecution(HandlerKey key, HandlerExecution execution) {
        if (handlerExecutions.putIfAbsent(key, execution) != null) {
            throw new IllegalStateException("Ambiguous mapping 에러! 이미 등록된 매핑입니다: [" + key + "]");
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        // RequestMapping에 requestMethod가 없다면 모든 HTTP method 넣어주기
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return requestMethods;
    }
}

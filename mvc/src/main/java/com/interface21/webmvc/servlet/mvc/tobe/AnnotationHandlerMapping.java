package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        // 1. basePackage에서 Controller 어노테이션 붙은 모든 클래스 가져오기
        for (Class<?> controllerClass : controllerClasses) {
            // 2. 컨트롤러 객체 생성
            Object controller = controllerClass
                    .getDeclaredConstructor()
                    .newInstance();

            // 3. 컨트롤러 내 Method 배열 반복
            for (Method method : controllerClass.getDeclaredMethods()) {
                // 4. RequestMapping 어노테이션에 사용된 값 객체 생성
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                // 5-1. RequestMapping 어노테이션이 붙은 Method가 없다면 넘어가기
                if (requestMapping == null) {
                    continue;
                }

                // 5-2. RequestMapping에 requestMethod가 없다면 모든 HTTP method 넣어주기
                if (requestMapping.method().length == 0) {
                    addAllMethod(requestMapping, controller, method);
                    continue;
                }

                // 5-3. RequestMapping의 method부분에 맞는 값들 넣어주기
                for (final RequestMethod requestMethod : requestMapping.method()) {
                    HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
                    // 이미 같은 URL과 Method 조합이 존재하는지 검사
                    validateDuplicate(key);
                    HandlerExecution execution = new HandlerExecution(controller, method);

                    handlerExecutions.put(key, execution);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(url, method));
    }

    private void addAllMethod(RequestMapping requestMapping, Object controller, Method method) {
        for (RequestMethod requestMethod : RequestMethod.values()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            validateDuplicate(key);
            HandlerExecution execution = new HandlerExecution(controller, method);

            handlerExecutions.put(key, execution);
        }
    }

    private void validateDuplicate(HandlerKey key) {
        if (handlerExecutions.containsKey(key)) {
            throw new IllegalStateException(
                    "Ambiguous mapping 에러! 이미 등록된 매핑입니다: [" + key + "]"
            );
        }
    }
}

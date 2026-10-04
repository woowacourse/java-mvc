package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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

    public void initialize() {
        log.info("핸들러 매핑 초기화 시작: 탐색 패키지={}", Arrays.toString(basePackage));
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            Class<?> clazz = entry.getKey();
            Object controller = entry.getValue();

            for (Method declaredMethod : clazz.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    registerHandler(controller, declaredMethod);
                }
            }
        }
        log.info("핸들러 매핑 초기화 완료: 컨트롤러 {}개, 매핑 {}개",
                controllers.size(), handlerExecutions.size());
    }

    private void registerHandler(Object controller, Method declaredMethod) {
        RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
        String value = annotation.value();
        RequestMethod[] method = annotation.method();

        if (method.length == 0) {
            method = RequestMethod.values();
        }
        HandlerExecution execution = new HandlerExecution(controller, declaredMethod);

        for (RequestMethod requestMethod : method) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            handlerExecutions.put(handlerKey, execution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(key);

        if (handlerExecution == null) {
            throw new NoSuchElementException("매핑된 핸들러가 없습니다: HTTP 메서드=" + requestMethod + ", URL=" + requestURI);
        }
        return handlerExecution;
    }
}

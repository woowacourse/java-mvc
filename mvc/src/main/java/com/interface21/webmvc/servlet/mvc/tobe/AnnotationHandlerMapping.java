package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
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

    // 클래스 탐색 -> 객체 생성 -> 매핑 메서드 탐색 -> Map 등록
    public void initialize() {
        try {
            // controller 어노테이션이 붙은 클래스 탐색
            ControllerScanner scanner = new ControllerScanner();
            Map<Class<?>, Object> controllers = scanner.scan(basePackage);

            for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
                Class<?> clazz = entry.getKey();
                Object controller = entry.getValue();

                Set<Method> methods = ReflectionUtils.getAllMethods(clazz,
                        ReflectionUtils.withAnnotation(RequestMapping.class));
                for (final Method method : methods) {

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    String url = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();

                    // Map 등록
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }

                }
            }


        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    // 요청 정보 추출 -> 키 생성 -> Map 조회
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey requestHandlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(requestHandlerKey);
    }
}

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

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        // @Controller 클래스 - 인스턴스 맵
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        controllers.keySet().forEach(clazz -> {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(clazz,
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            mappingAllHandlerMethods(clazz, allMethods, controllers);
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mappingAllHandlerMethods(Class<?> clazz, Set<Method> allMethods, Map<Class<?>, Object> controllers) {
        allMethods.forEach(method -> handlerMapping(
                        method,
                        controllers.get(clazz),
                        method.getDeclaredAnnotation(RequestMapping.class)
                )
        );
    }

    private void handlerMapping(Method method, Object controller, RequestMapping requestMapping) {
        if (requestMapping == null) {   // 없으면 패스
            return;
        }
        RequestMethod[] requestMethods = requestMapping.method();
        // 별도의 요청 메서드 설정 없으면 모든 요청 메서드 지원
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod httpMethod : requestMethods) {
            // 키 설정 : {경로, 요청 메서드}
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            // {키, 핸들러(키를 처리할 메서드)} 등록
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = normalize(request.getRequestURI());
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(request.getMethod())));
    }

    private String normalize(String uri) {
        if (uri == null || uri.isEmpty()) {
            return "/";
        }
        // 마지막 / 제거
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        // 중복 슬래시 정리
        return uri.replaceAll("//+", "/");
    }
}

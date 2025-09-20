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

    /**
     * ControllerScanner 클래스를 통해 베이스 패키지 내부의 컨트롤러 클래스와 해당 인스턴스들을 가져옵니다. 리플랙션을 통해 컨트롤러 클래스 내부를 스캔하며 RequestMapping 어노테이션이
     * 붙은 메서드들을 가져옵니다. 각 메서드마다 RequestMapping 어노테이션의 경로와 요청 메서드(Get, Put..)를 HandlerKey로 사용하고, 해당 요청을 처리할 메서드와 클래스의 인스턴스
     * 정보를 Handler로 HandlerExecutions 에 매핑합니다.
     */
    @Override
    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        // @Controller 클래스 - 인스턴스 맵
        Map<Class<?>, Object> controllerRegistry = controllerScanner.getControllerRegistry();
        controllerRegistry.entrySet().forEach(entry -> {
            Set<Method> allMethods = ReflectionUtils.getAllMethods(entry.getKey(),
                    ReflectionUtils.withAnnotation(RequestMapping.class));
            mappingAllHandlerMethods(allMethods, entry.getValue());
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public boolean support(HttpServletRequest request) {
        return getHandler(request) != null;
    }

    private void mappingAllHandlerMethods(Set<Method> allMethods, Object controller) {
        allMethods.forEach(method -> handlerMapping(
                        method,
                        controller,
                        method.getDeclaredAnnotation(RequestMapping.class)
                )
        );
    }

    private void handlerMapping(Method method, Object controller, RequestMapping requestMapping) {
        if (requestMapping == null) {
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

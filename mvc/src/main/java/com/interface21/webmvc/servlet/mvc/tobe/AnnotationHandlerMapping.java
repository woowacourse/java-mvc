package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    // HandlerKey(URI + HTTP Method) → HandlerExecution(실행 대상 객체 + 메서드) 매핑 저장소
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    // 스캔할 base package 목록
    private final Object[] basePackages;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.handlerExecutions = new HashMap<>();
        this.basePackages = basePackages;
    }

    /**
     * 초기화 메서드: handlerExecutions에 매핑 정보를 등록
     */
    public void initialize() {
        try {
            handlerExecutions.putAll(initHandlerExecutions());
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (Exception e) {
            log.error("error to initialize AnnotationHandlerMapping");
            throw new RuntimeException(e);
        }
    }

    /**
     * 조회 메서드: 요청(HttpServletRequest)에 맞는 핸들러(HandlerExecution)를 조회
     */
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    private Map<HandlerKey, HandlerExecution> initHandlerExecutions() throws Exception {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (Object basePackage : basePackages) {                               // basePackages 내부의 모든 클래스에서
            var reflections = new Reflections(basePackage);                     // Reflections 라이브러리로
            var classes = reflections.getTypesAnnotatedWith(Controller.class);  // @Controller 애노테이션이 붙은 클래스들 검색

            var classExecutions = createClassExecutions(classes);               // 클래스 HandlerExecutions 생성
            handlerExecutions.putAll(classExecutions);
        }

        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createClassExecutions(final Set<Class<?>> classes) throws Exception {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (Class<?> controller : classes) {                                   // 컨트롤러 클래스 별
            handlerExecutions.putAll(createControllerExecutions(controller));   // 컨트롤러 클래스 HandlerExecutions 생성
        }

        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createControllerExecutions(final Class<?> controller) throws Exception {
        var instance = controller.getDeclaredConstructor().newInstance();           // 컨트롤러 인스턴스를 생성(newInstance)
        var methods = controller.getDeclaredMethods();                              // 클래스 안의 모든 메서드 가져오기

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (Method method : methods) {                                             // 컨트롤러 클래스 메서드별
            handlerExecutions.putAll(createMethodHandlers(instance, method));       // 메서드 HandlerExecutions 생성
        }

        return handlerExecutions;
    }

    private Map<HandlerKey, HandlerExecution> createMethodHandlers(final Object instance, final Method method) {
        var requestMapping = method.getDeclaredAnnotation(RequestMapping.class);      // @RequestMapping 읽기
        var requestMethods = requestMapping.method();                                 // 지원하는 HTTP Method 배열

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        for (RequestMethod requestMethod : requestMethods) {
            var handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            var handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }

        return handlerExecutions;
    }
}

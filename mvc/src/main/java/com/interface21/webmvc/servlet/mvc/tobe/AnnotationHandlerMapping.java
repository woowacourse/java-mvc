package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage); // 기본 패키지 경로 설정된 리플랙션 객체 생성
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);    // @Controller 붙은 클래스 스캔
        for(Class<?> clazz : controllers){
            try {
                Object controller = clazz.getDeclaredConstructor().newInstance();   // 인스턴스 생성
                for(Method method : controller.getClass().getDeclaredMethods()){    // 전체 메서드 스캔
                    RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class); // @RequestMapping 붙은 메서드 스캔
                    if(requestMapping==null){   // 없으면 패스
                        continue;
                    }
                    RequestMethod[] requestMethods = requestMapping.method();
                    if(requestMethods.length == 0){ // 별도의 요청 메서드 설정 없으면 전체 범위
                       requestMethods = RequestMethod.values();
                    }
                    for(RequestMethod httpMethod : requestMethods){
                        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod); // {경로, 요청 메서드}로 키 설정
                        handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));    // {키, 핸들러(키를 처리할 메서드)} 등록
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }
    
    public Object getHandler(final HttpServletRequest request) {
        String uri = normalize(request.getRequestURI());
        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.valueOf(request.getMethod())));
    }

    private String normalize(String uri) {
        if (uri == null || uri.isEmpty()) return "/";
        // 마지막 / 제거
        if (uri.length() > 1 && uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        // 중복 슬래시 정리
        return uri.replaceAll("//+", "/");
    }
}

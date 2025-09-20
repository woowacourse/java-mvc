package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.reflections.Reflections;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    /**
     * basePackage 안의 Controller 클래스 모두 찾는다 각 Controller 클래스의 인스턴스를 생성한다 RequestMapping 어노테이션이 붙은 메서드만 골라낸다 (url,
     * requestMethod)키로 HandlerExecution를 등록한다
     */
    public void initialize() {

        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        try {
            for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
                Class<?> clazz = entry.getKey();
                Object instance = entry.getValue();

                // @RequestMapping 붙은 메서드만 골라냄 ex) findUserId(), save()
                List<Method> methods = getRequestMappingMethods(clazz);
                // HandlerExecution 테이블에 등록
                methods.forEach(method -> putHandlerExecutionByRequestMapping(handlerExecutions, instance, method));
            }
        } catch (Exception e) {
            throw new IllegalStateException("HandlerMapping initialization failed", e);
        }
    }

    /**
     * HttpServletRequest를 통해 실행할 HandlerExecution을 찾는다 요청에서 uri, http method 추출해서 handlerKey 만들고 이 키로 handlerExecution
     * 조회
     */
    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        return handlerExecutions.get(handlerKey);
    }

    private List<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .peek(method -> method.setAccessible(true))
                .toList();
    }

    private void putHandlerExecutionByRequestMapping(
            final Map<HandlerKey, HandlerExecution> handlerExecutions,
            final Object instance,
            final Method method
    ) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }
        String url = mapping.value(); // ex "/get-test"
        RequestMethod[] requestMethods = mapping.method(); // ex GET, POST, ...

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution execution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, execution);
        }
    }
}

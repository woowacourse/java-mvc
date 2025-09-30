package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        log.info("=== AnnotationHandlerMapping initialize start ===");

        // @Controller 어노테이션이 붙은 클래스 가져오기
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // @RequestMapping 어노테이션을 가진 메서드 가져오기
        final Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());
        for (final Method method : requestMappingMethods) {
            // RequestMapping 속성 값 추출
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final String url = requestMapping.value();
            final RequestMethod[] requestMethods = requestMapping.method();

            // RequestMapping 속성 값으로 HandlerKey 리스트 생성 후 handlerExecutions 매핑 처리
            final List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);
            final HandlerExecution handlerExecution = new HandlerExecution(controllers.get(method.getDeclaringClass()),
                    method);
            for (final HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
        log.info("=== AnnotationHandlerMapping initialize end ===");
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        List<RequestMethod> targetRequestMethods = List.of(requestMethods);
        if (targetRequestMethods.isEmpty()) {
            targetRequestMethods = List.of(RequestMethod.values());
        }

        final List<HandlerKey> handlerKeys = new ArrayList<>();
        for (final RequestMethod requestMethod : targetRequestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }
        return handlerKeys;
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        final Set<Method> methods = new HashSet<>();
        for (final Class<?> controllerClazz : controllerClasses) {
            methods.addAll(ReflectionUtils.getMethodsAnnotatedWith(controllerClazz, RequestMapping.class));
        }
        return methods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String httpMethod = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(httpMethod);

        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

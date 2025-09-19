package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(url, method);
        HandlerExecution handlerExecution = handlerExecutions.get(key);
        if (handlerExecution == null) {
            log.error("Cannot find matching controller. url : {}, method: {}", url, method);
            throw new NoSuchElementException();
        }
        return handlerExecution;
    }

    public void initialize() {
        for (Object basePackage : basePackages) {
            ControllerScanner controllerScanner = new ControllerScanner(new Reflections(basePackage));
            Map<Class<?>, Object> controllers = controllerScanner.getControllers(); // @Controller가 붙어있는 모든 클래스 스캔해옴
            addHandlerExecutions(controllers);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllerMap) {
        for (Entry<Class<?>, Object> entry : controllerMap.entrySet()) {
            Set<Method> requestMappingMethods = getRequestMappingMethods(entry.getKey());
            for (Method method : requestMappingMethods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
                handlerKeys.forEach(handlerKey ->
                        handlerExecutions.put(
                                handlerKey,
                                new HandlerExecution(entry.getValue(), method))
                );
            }
        }
    }

    private Set<Method> getRequestMappingMethods(Class<?> controller) {
        Set<Method> methods = new HashSet<>();
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        /*
         * 빈 request method일 경우, default로 모든 HTTP 메서드 매핑
         */
        if (requestMethods == null || requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .toList();
    }
}

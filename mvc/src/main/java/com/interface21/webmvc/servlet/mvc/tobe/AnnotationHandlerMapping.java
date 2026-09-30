package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
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

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String uri = requestMapping.value();
                    RequestMethod[] requestMethods = resolveRequestMethods(requestMapping);

                    HandlerExecution handlerExecution = createHandlerExecution(clazz, method);
                    for (RequestMethod requestMethod : requestMethods) {
                        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

                        if (handlerExecutions.get(handlerKey) != null) {
                            throw new IllegalStateException("중복된 핸들러가 등록되었습니다: " + uri + " " + requestMethod);
                        }

                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return RequestMethod.values();
        }
        return methods;
    }

    private HandlerExecution createHandlerExecution(Class<?> clazz, Method method) {
        try {
            Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
            Object controller = constructor.newInstance();
            return new HandlerExecution(controller, method);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("컨트롤러 생성자 생성 실패: " + clazz.getName(), e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + clazz.getName(), e.getCause());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("getHandler 호출, requestURI = {}, httpMethod = {}", requestURI, httpMethod.name());

        HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestURI, httpMethod));

        if (handlerExecution == null) {
            throw new NoSuchElementException("요청을 처리할 핸들러가 없습니다: " + requestURI + " " + httpMethod.name());
        }

        return handlerExecution;
    }
}

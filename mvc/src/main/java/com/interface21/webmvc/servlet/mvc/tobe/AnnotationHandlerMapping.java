package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            Object controller = createController(controllerClass);
            for (Method method : controllerClass.getDeclaredMethods()) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (mapping == null) {
                    continue;
                }

                if (!method.trySetAccessible()) {
                    throw new IllegalStateException("핸들러에 접근할 수 없습니다: " + method);
                }

                RequestMethod[] methods = mapping.method();
                if (methods.length == 0) {
                    methods = RequestMethod.values();
                }

                String uri = mapping.value();
                for (RequestMethod requestMethod : methods) {
                    HandlerExecution existing = handlerExecutions.putIfAbsent(
                            new HandlerKey(uri, requestMethod),
                            new HandlerExecution(controller, method)
                    );
                    if (existing != null){
                        throw new IllegalArgumentException("중복된 키 값 입니다.");
                    }
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    private Object createController(Class<?> controllerClass) {
        try {
            return ReflectionUtils.accessibleConstructor(controllerClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러 생성 실패: " + controllerClass.getName(), e
            );
        }
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final Object basePackage : basePackages) {
            registerControllersInPackage(basePackage);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        final HandlerExecution findHandlerExecution = handlerExecutions.getOrDefault(handlerKey, null);
        if (findHandlerExecution == null) {
            throw new NoSuchElementException("해당 요청을 처리활 수 있는 핸들러가 없습니다.");
        }
        return findHandlerExecution;
    }

    private void registerControllersInPackage(final Object basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {
            registerController(controller);
        }
    }

    private void registerController(final Class<?> controller) {
        final Object instance = createControllerInstance(controller);
        final Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(
                controller,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
        for (final Method method : requestMappingMethods) {
            registerHandlerMethod(method, instance);
        }
    }

    private void registerHandlerMethod(final Method method, final Object instance) {
        final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        final String url = annotation.value();
        final RequestMethod[] requestMethods = getRequestMethods(annotation);

        for (final RequestMethod requestMethod : requestMethods) {
            log.info(
                    "등록 완료 : url ={}, method = {}, handlerMethod = {}",
                    url,
                    requestMethod.name(),
                    method.getName()
            );
            handlerExecutions.put(
                    new HandlerKey(url, requestMethod),
                    new HandlerExecution(instance, method)
            );
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping annotation) {
        if (annotation.method().length == 0) {
            return RequestMethod.values();
        }
        return annotation.method();
    }

    private Object createControllerInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("기본 생성자가 존재하지 않습니다.");
        }
    }
}

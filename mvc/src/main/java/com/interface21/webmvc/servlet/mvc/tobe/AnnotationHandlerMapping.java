package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
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
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            final Object controller = createInstance(aClass);

            for (Method method : aClass.getMethods()) {
                if (isAnnotationPresent(method, RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    enrollHandler(requestMapping, method, controller);
                }
            }
        }

    }

    private boolean isAnnotationPresent(Method method, Class<? extends Annotation> annotation) {
        return method.isAnnotationPresent(annotation);
    }

    @Nonnull
    private Object createInstance(Class<?> aClass) {
        try {
            return ReflectionUtils.accessibleConstructor(aClass).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + aClass.getName(), e);
        }
    }

    private void enrollHandler(RequestMapping requestMapping, Method method, Object controller) {
        String uri = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
            HandlerExecution handlerExecution = (request, response) ->
                    (ModelAndView) method.invoke(controller, request, response);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}

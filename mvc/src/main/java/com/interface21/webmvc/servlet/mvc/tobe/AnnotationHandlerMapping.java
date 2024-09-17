package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.forEach(this::addHandlerExecution);
    }

    private void addHandlerExecution(Class<?> controller) {
        Method[] controllerMethods = controller.getDeclaredMethods();

        for (Method controllerMethod : controllerMethods) {
            Annotation annotation = controllerMethod.getAnnotation(RequestMapping.class);
            try {
                Method value = annotation.getClass().getDeclaredMethod("value");
                Method method = annotation.getClass().getDeclaredMethod("method");

                String uri = (String) value.invoke(annotation);
                RequestMethod[] requestMethods = (RequestMethod[]) method.invoke(annotation);
                log.info("value = {}, method = {}", uri, requestMethods);

                if (requestMethods.length == 0) {
                    requestMethods = RequestMethod.values();
                }

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controllerMethod);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }

            } catch (NoSuchMethodException e) {
                log.error("@RequestMapping 리플렉션 에러 발생");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);

        if (handlerExecutions.containsKey(handlerKey)) {
            return handlerExecutions.get(handlerKey);
        }
        throw new UnsupportedOperationException("지원하지 않는 요청입니다.");
    }
}

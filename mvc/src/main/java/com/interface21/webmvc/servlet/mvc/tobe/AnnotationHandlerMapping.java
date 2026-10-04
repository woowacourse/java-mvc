package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses =  reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses){
            try {
                final Object controller = controllerClass.getDeclaredConstructor().newInstance();
                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods){
                    if (method.getAnnotation(RequestMapping.class) != null){
                        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        RequestMethod[] requestMethods = requestMapping.method();
                        String url = requestMapping.value();
                        if (requestMethods.length==0){
                            // todo : 모든 http 메서드랑 매핑
                        }
                        for (RequestMethod rm : requestMethods){
                            // todo: 중복된 매핑 있으면 예외처리
                            handlerExecutions.put(new HandlerKey(url, rm), new HandlerExecution(controller, method));
                        }
                    }
                }

            } catch (ReflectiveOperationException e){
                throw new IllegalArgumentException("초기화 실패", e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        String url = request.getRequestURI();

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try{
            scanController(basePackage);
        }catch(Exception e){
            log.info("error while initializing AnnotationHandlerMapping");
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }

    private void scanController(Object[] basePackage) throws Exception {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for(Class<?> controllerClass : controllerClasses) {
            Object controller = ReflectionUtils.accessibleConstructor(controllerClass).newInstance();

            Method[] methods = controllerClass.getMethods();
            for(Method method : methods) {
                // 특정 컨트롤러의 RequestMapping이 붙은 메서드만
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                if(requestMapping == null) {
                    continue;
                }

                String url = requestMapping.value();
                RequestMethod[] httpMethods = requestMapping.method();
                if (httpMethods.length == 0) {
                    httpMethods = RequestMethod.values();
                }

                for(RequestMethod requestMethod : httpMethods) {
                    HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                    if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                        throw new IllegalStateException("중복되는 핸들러입니다.: " + handlerKey);
                    }
                }

            }
        }
    }

}

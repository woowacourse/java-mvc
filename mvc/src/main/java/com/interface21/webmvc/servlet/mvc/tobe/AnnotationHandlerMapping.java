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
        //패키지를 뒤진다.
        final Reflections reflections = new Reflections(basePackage);

        //@Controller만 찾는다
        final Set<Class<?>> controllerClasses =
                reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            //컨트롤러 객체를 만든다.
            final Object controller = createController(controllerClass);

            //메서드를 전부 살핀다.
            for (Method method : controllerClass.getDeclaredMethods()) {
                //@RequestMapping 메서드만 고른다.
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }

                registerHandler(controller, method);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Object createController(final Class<?> controllerClass) {
        try {
            return controllerClass
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(),
                    e
            );
        }
    }


    private void registerHandler(final Object controller, final Method method) {
        // @RequestMapping 안의 값을 꺼낸다
        //@RequestMapping(value = "/get-test", method = RequestMethod.GET) 자체를 객체로 가져옴
        final RequestMapping requestMapping =
                method.getAnnotation(RequestMapping.class);

        final RequestMethod[] requestMethods = requestMapping.method();

        if (requestMethods.length == 0) {
            for (RequestMethod requestMethod : RequestMethod.values()) {
                registerHandler(requestMapping.value(), requestMethod, controller, method);
            }
            return;
        }

        for (RequestMethod requestMethod : requestMethods) {
            registerHandler(requestMapping.value(), requestMethod, controller, method);
        }
    }

    private void registerHandler(final String url, final RequestMethod requestMethod,
                                 final Object controller, final Method method) {
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(url, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
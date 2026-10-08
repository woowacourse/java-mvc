package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.scan();
        controllers.forEach(this::registerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(Class<?> controllerClass, Object controller) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            RequestMapping mapping = declaredMethod.getAnnotation(RequestMapping.class);
            if (mapping == null) {
                continue;
            }
            validateHandlerMethod(declaredMethod);
            String url = mapping.value();
            RequestMethod[] method = mapping.method();
            if (method.length == 0) {
                method = RequestMethod.values();
            }
            for (RequestMethod requestMethod : method) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                if (handlerExecutions.get(handlerKey) != null) {
                    throw new IllegalStateException("중복된 요청 매핑입니다.: " + handlerKey);
                }
                handlerExecutions.put(handlerKey, new HandlerExecution(controller, declaredMethod));
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }

    private void validateHandlerMethod(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2
                || parameterTypes[0] != HttpServletRequest.class
                || parameterTypes[1] != HttpServletResponse.class) {
            throw new IllegalStateException("잘못된 컨트롤러 매개변수 입니다.: " + method);
        }
        if (method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException("잘못된 컨트롤러 반환타입 입니다.: " + method);
        }
    }
}

package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object getHandler() {
        return handler;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        try {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Object[] argument = new Object[parameterTypes.length];

            for (int idx = 0; idx < parameterTypes.length; idx++) {
                if (parameterTypes[idx].isAssignableFrom(HttpServletRequest.class)) {
                    argument[idx] = request;
                }
                if (parameterTypes[idx].isAssignableFrom(HttpServletResponse.class)) {
                    argument[idx] = response;
                }
                if (parameterTypes[idx].isAssignableFrom(ModelAndView.class)) {
                    argument[idx] = modelAndView;
                }
            }
            return method.invoke(handler, argument);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("핸들러 실행을 실패했습니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("핸들러 실행을 실패했습니다. 이유: " + e.getMessage());
        }
    }
}

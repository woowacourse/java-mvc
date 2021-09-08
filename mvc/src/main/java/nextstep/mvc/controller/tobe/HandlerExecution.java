package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.mvc.view.JspView;
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

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            final Object invoke = method.invoke(handler, request, response);
            if (invoke instanceof ModelAndView) {
                return (ModelAndView) invoke;
            }
            return new ModelAndView(new JspView((String) invoke));
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("핸들러 실행을 실패했습니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("핸들러 실행을 실패했습니다. 이유: " + e.getMessage());
        }
    }
}

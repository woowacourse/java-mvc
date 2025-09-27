package com.interface21.webmvc.servlet.mvc.execution;

import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecution.class);

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            return (ModelAndView) method.invoke(handler, request, response);
        } catch (InvocationTargetException e) {
            log.error("핸들러 메서드 실행 중 예외 발생: {}.{} - {}",
                    handler.getClass().getSimpleName(),
                    method.getName(),
                    e.getTargetException().getMessage(),
                    e.getTargetException()
            );
            throw (Exception) e.getTargetException();
        } catch (IllegalAccessException e) {
            log.error("핸들러 메서드에 접근할 수 없습니다: {}.{}",
                    handler.getClass().getSimpleName(),
                    method.getName(), e
            );
            throw new RuntimeException();
        }
    }
}

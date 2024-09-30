package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object object) {
        return object instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(Object object, HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = (HandlerExecution) object;
        try {
            return handlerExecution.handle(request, response);
        } catch (Exception e) {
            throw new IllegalStateException("핸들러의 요청을 처리하던 중 예외가 발생했습니다." + e.getMessage());
        }
    }
}

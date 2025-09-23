package com.interface21.webmvc.servlet.handler;

import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerReturnValueHandler {

    public static void handle(Object handlerReturnValue, HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) throws ServletException {
        if (handlerReturnValue instanceof ModelAndView modelAndView) {
            modelAndView.render(httpServletRequest, httpServletResponse);
        } else if (handlerReturnValue instanceof String viewName) {
            View view = new JspView(viewName);
            new ModelAndView(view).render(httpServletRequest, httpServletResponse);
        } else {
            throw new ServletException("핸들러 처리 과정 도중 오류가 발생하였습니다.");
        }
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (handler instanceof Controller) {
            return new ModelAndView(extractView(handler, request, response));
        }
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        throw new IllegalArgumentException("처리할 수 없는 핸들러 타입입니다: " + handler.getClass().getName());
    }

    private JspView extractView(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        return new JspView(viewName);
    }
}

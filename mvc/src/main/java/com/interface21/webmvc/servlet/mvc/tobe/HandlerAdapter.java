package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

public class HandlerAdapter {

    public ModelAndView handle(final Object handler, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (handler instanceof Controller) {
            final String result = ((Controller) handler).execute(req, res);
            return new ModelAndView(new JspView(result));
        }
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution)handler).handle(req, res);
        }
        throw new IllegalArgumentException("실행할 수 없는 타입의 handler 입니다.");
    }
}

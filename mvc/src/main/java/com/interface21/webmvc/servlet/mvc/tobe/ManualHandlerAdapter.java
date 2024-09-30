package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    public boolean canHandle(final Object handler) {
        return handler instanceof Controller;
    }

    public ModelAndView handle(final Object handler, final HttpServletRequest req, final HttpServletResponse res)
            throws Exception {
        if (canHandle(handler)) {
            final String result = ((Controller) handler).execute(req, res);
            return new ModelAndView(new JspView(result));
        }
        throw new IllegalArgumentException("실행할 수 없는 타입의 handler 입니다.");
    }
}

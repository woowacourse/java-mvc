package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res, final Object handler)
            throws Exception {
        return new ModelAndView(
                new JspView(
                        ((Controller) handler).execute(req, res)
                )
        );
    }
}

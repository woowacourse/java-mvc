package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        if (!(handler instanceof Controller)) {
            throw new UnsupportedOperationException("지원하지 않는 핸들러 타입입니다. : " + handler);
        }
        final String rawView = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(rawView));
    }

    @Override
    public boolean canHandle(final Object handler) {
        return handler instanceof Controller;
    }
}

package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!support(handler)) {
            throw new IllegalArgumentException("지원하지 않는 핸들러입니다.");
        }
        String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(JspView.from(viewName));
    }
}

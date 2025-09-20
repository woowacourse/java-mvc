package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (!(handler instanceof Controller)) {
            throw new IllegalStateException("ManualHandlerAdapter는 Controller 클래스만을 실행할 수 있습니다");
        }
        final String viewName = ((Controller) handler).execute(request, response);
        return ModelAndView.withoutModel(JspView.from(viewName));
    }
}

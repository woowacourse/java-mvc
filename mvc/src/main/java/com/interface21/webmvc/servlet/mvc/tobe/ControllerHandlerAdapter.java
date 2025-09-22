package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter extends AbstractHandlerAdapter<Controller> {
    @Override
    protected ModelAndView handleInternal(
            final Controller handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        String viewName = handler.execute(request, response);
        //TODO: 구체 View(Jsp)에 의존 중. ViewResolver를 넣는다면 이에 맞춰 수정 필요
        return new ModelAndView(new JspView(viewName));
    }
}

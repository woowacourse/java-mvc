package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.HandlerType;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdapter extends AbstractHandlerAdapter {

    @Override
    protected HandlerType supportedType() {
        return HandlerType.CONTROLLER;
    }

    @Override
    protected ModelAndView doHandle(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final String viewName = ((Controller) handler.instance()).execute(request, response);
        return ModelAndView.withoutModel(JspView.from(viewName));
    }
}

package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handleExecution) {
        return handleExecution instanceof Controller;
    }

    @Override
    public ModelAndView doInternalService(final HttpServletRequest request,
                                          final HttpServletResponse response,
                                          final Object method
    ) throws Exception {
        final Controller controller = (Controller) method;
        final String path = controller.execute(request, response);

        return new ModelAndView(new JspView(path));
    }
}

package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(ManualHandlerAdapter.class);

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object object)
            throws Exception {
        String viewName = ((Controller) object).execute(request, response);
        View view = new JspView(viewName);
        return new ModelAndView(view);
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }
}

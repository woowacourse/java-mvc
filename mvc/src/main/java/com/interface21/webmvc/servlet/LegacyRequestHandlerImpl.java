package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LegacyRequestHandlerImpl implements RequestHandler {

    private final Controller controller;

    public LegacyRequestHandlerImpl(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
        String viewName = controller.execute(httpRequest, httpResponse);
        return new ModelAndView(new JspView(viewName));
    }
}

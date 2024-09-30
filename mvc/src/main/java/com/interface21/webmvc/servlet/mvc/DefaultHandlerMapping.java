package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultHandlerMapping implements HandlerMapping {

    @Override
    public void initialize() {
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return new Object() {
            ModelAndView handle() {
                return new ModelAndView(new JspView("redirect:/404.jsp"));
            }
        };
    }
}

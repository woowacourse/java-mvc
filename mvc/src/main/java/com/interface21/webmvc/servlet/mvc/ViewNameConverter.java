package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewNameConverter implements ViewResolver {

    @Override
    public boolean canHandle(Object result) {
        return result instanceof String;
    }

    @Override
    public ModelAndView convert(Object result) {
        String viewName = (String) result;
        return new ModelAndView(new JspView(viewName));
    }
}

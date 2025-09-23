package com.interface21.webmvc.servlet.mvc.view;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

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

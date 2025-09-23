package com.interface21.webmvc.servlet.mvc.view;

import com.interface21.webmvc.servlet.ModelAndView;

public class ModelAndViewConverter implements ViewResolver {

    @Override
    public boolean canHandle(Object result) {
        return result instanceof ModelAndView;
    }

    @Override
    public ModelAndView convert(Object result) {
        return (ModelAndView) result;
    }
}

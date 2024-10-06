package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

public class ViewResolver {

    private final ModelAndView modelAndView;

    public ViewResolver(final ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }

    public View resolve() {
        return modelAndView.getView();
    }
}

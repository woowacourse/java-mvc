package com.interface21.webmvc.servlet.mvc.view;

import com.interface21.webmvc.servlet.ModelAndView;

public interface ViewResolver {

    boolean canHandle(Object result);

    ModelAndView convert(Object result);
}

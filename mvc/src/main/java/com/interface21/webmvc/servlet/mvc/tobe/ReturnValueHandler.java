package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;

public interface ReturnValueHandler {

    boolean support(Object returnValue);

    ModelAndView handle(Object returnValue);
}

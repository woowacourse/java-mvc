package com.interface21.webmvc.servlet.mvc.tobe.returnvaluehandler;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.ReturnValueHandler;
import com.interface21.webmvc.servlet.view.JsonView;

public class JsonReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean supports(Object returnValue) {
        return !(returnValue instanceof ModelAndView || returnValue instanceof String);
    }

    @Override
    public ModelAndView handle(Object returnValue) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("value", returnValue);
        return modelAndView;
    }
}

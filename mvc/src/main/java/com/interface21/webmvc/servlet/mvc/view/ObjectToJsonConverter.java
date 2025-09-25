package com.interface21.webmvc.servlet.mvc.view;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;

public class ObjectToJsonConverter implements ViewResolver {

    @Override
    public boolean canHandle(Object result) {
        return result != null
            && !(result instanceof String)
            && !(result instanceof ModelAndView);
    }

    @Override
    public ModelAndView convert(Object result) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("data", result);
        return modelAndView;
    }
}

package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.exception.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerAdapterrrr {
    public static void handle(HttpServletRequest request, HttpServletResponse response,
                               Object handler) throws Exception {
        ModelAndView mav;
        if (handler instanceof Controller) {
            mav = ((Controller)handler).execute(request, response);
        } else if (handler instanceof HandlerExecution) {
            mav = ((HandlerExecution)handler).handle(request, response);
        } else {
            throw new NoHandlerFoundException("No Handler Found!");
        }
        mav.getView().render(mav.getModel(), request, response);
    }
}

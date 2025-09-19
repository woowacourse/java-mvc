package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlerAdapter {
    public static void handle(HttpServletRequest request, HttpServletResponse response,
                               HandlerMapping handlerMapping) throws Exception {
        ModelAndView mav;
        Object handler = handlerMapping.getHandler(request);
        if (handler instanceof Controller) {
            mav = ((Controller)handler).execute(request, response);
        } else if (handler instanceof HandlerExecution) {
            mav = ((HandlerExecution)handler).handle(request, response);
        } else {
            throw new NoHandlerFoundException("No Handler Found!");
        }
        mav.getView().render(Map.of(), request, response);
    }
}

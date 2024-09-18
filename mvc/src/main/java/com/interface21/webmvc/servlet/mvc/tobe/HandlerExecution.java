package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Handler handler;

    public HandlerExecution(Handler handler) {
        this.handler = handler;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        return handler.handle(request, response);
    }
}

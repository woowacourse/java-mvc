package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutor {

    public HandlerExecutor() {
    }

    public ModelAndView execute(HandlerAdapter handlerAdapter, Object handler, HttpServletRequest request,
                                HttpServletResponse response) throws Exception {
        return handlerAdapter.handle(request, response, handler);
    }
}

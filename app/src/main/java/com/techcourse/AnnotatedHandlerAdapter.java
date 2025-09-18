package com.techcourse;

import com.interface21.webmvc.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class AnnotatedHandlerAdapter implements HandlerAdapter {

    public AnnotatedHandlerAdapter() {
    }

    @Override
    public void doHandle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = ((HandlerExecution) handler).handle(request);
        Map<String, ?> model = modelAndView.getModel();
        modelAndView.getView()
                .render(model, request, response);
    }

    @Override
    public boolean isProcessable(Object handler) {
        return handler instanceof HandlerExecution;
    }
}

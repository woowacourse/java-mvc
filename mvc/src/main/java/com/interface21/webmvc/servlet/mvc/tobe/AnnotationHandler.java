package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandler implements Handler{

    private HandlerExecution execution;

    public AnnotationHandler(HandlerExecution execution) {
        this.execution = execution;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return execution.handle(request, response);
    }
}

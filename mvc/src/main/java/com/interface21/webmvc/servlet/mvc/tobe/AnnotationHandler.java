package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandler implements Handler{

    private HandlerExecution execution;

    public AnnotationHandler(HandlerExecution execution) {
        this.execution = execution;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = execution.handle(request, response);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}

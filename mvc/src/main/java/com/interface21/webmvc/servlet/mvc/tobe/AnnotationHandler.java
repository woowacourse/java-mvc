package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandler implements Handler {

    private final HandlerExecution handlerExecution;

    public AnnotationHandler(HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ModelAndView modelAndView = handlerExecution.handle(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}

package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandler implements Handler {

    private final HandlerExecution handlerExecution;

    public AnnotationHandler(HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            return handlerExecution.handle(request, response);
        } catch (Throwable e) {
            if (e.getCause() instanceof BadRequestException) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return new ModelAndView(new JsonView());
            }
            throw new ServletException(e.getMessage());
        }
    }
}

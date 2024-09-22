package com.techcourse;

import static com.interface21.webmvc.servlet.view.JspView.REDIRECT_PREFIX;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandler implements Handler {

    private final Controller controller;

    public ManualHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            final String viewName = controller.execute(request, response);
            move(viewName, request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }


    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        View view = resolveView(viewName);
        ModelAndView modelAndView = new ModelAndView(view);
        view.render(modelAndView.getModel(), request, response);
    }

    private View resolveView(String viewName) {
        return new JspView(viewName);
    }
}

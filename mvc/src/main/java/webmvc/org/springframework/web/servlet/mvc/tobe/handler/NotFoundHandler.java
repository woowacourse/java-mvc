package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class NotFoundHandler implements Handler {

    private static final String NOT_FOUND_VIEW_NAME = "redirect:/404.jsp";

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response) {
        return new ModelAndView(new JspView(NOT_FOUND_VIEW_NAME));
    }
}

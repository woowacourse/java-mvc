package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultController {

    private static final DefaultController INSTANCE = new DefaultController();
    private static final String NOT_FOUND_FILE_NAME = "404.jsp";

    private DefaultController() {
    }

    public static DefaultController getInstance() {
        return INSTANCE;
    }

    public ModelAndView handleNotFound(
            final HttpServletRequest request,
            final HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new ModelAndView(new JspView(NOT_FOUND_FILE_NAME));
    }
}

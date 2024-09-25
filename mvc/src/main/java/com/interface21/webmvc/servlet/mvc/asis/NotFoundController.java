package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundController implements Controller {

    private static final String NOT_FOUND_FILE_NAME = "/404.jsp";

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return NOT_FOUND_FILE_NAME;
    }
}

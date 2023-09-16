package com.techcourse.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.CustomController;

public class MvcLogoutController implements CustomController {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(MvcUserSession.SESSION_KEY);
        return "redirect:/";
    }
}

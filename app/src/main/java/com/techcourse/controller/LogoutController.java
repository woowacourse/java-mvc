package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

public class LogoutController implements Controller {

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}

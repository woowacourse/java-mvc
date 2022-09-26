package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}

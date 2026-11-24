package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

import static com.interface21.web.bind.annotation.RequestMethod.GET;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = GET)
    public String execute(final HttpServletRequest request) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}

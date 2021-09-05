package com.techcourse.controller;

import jakarta.servlet.http.HttpSession;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) throws Exception {
        session.removeAttribute(UserSession.SESSION_KEY);
        return "redirect:/";
    }
}

package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(JspView.from("redirect:/"));
    }
}

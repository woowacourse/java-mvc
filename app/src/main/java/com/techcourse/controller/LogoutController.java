package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(JspView.redirectTo("/index.jsp"));
    }
}

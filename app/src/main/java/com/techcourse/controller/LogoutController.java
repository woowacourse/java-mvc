package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    private static final String REDIRECT_ROOT = "redirect:/";

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView getLogout(HttpServletRequest request, HttpServletResponse response) {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return convertStringToMav(REDIRECT_ROOT);
    }

    private ModelAndView convertStringToMav(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}

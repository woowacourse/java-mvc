package com.techcourse.controller.mvc;

import com.techcourse.controller.UserSession;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class MvcLogoutController {

    private static final Logger log = LoggerFactory.getLogger(MvcLogoutController.class);

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logoutUser(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}

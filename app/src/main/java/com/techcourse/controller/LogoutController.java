package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.view.JspView;
import com.interface21.webmvc.servlet.mvc.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

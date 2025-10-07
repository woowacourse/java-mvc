package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout")
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res)  {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        final String viewName = "redirect:/";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}

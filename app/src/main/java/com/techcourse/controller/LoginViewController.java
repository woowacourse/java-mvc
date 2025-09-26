package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLoginView(final HttpServletRequest req, final HttpServletResponse res) {
        final var viewName = getViewName(req);
        return new ModelAndView(new JspView(viewName));
    }

    private String getViewName(final HttpServletRequest req) {
        final var userFrom = UserSession.getUserFrom(req.getSession());
        if (userFrom.isEmpty()) {
            return "/login.jsp";
        }
        final var user = userFrom.get();
        log.info("logged in {}", user.getAccount());
        return "redirect:/index.jsp";
    }
}

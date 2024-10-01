package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest req) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView("redirect:/index.jsp");
                })
                .orElse(new ModelAndView("/login.jsp"));
    }
}

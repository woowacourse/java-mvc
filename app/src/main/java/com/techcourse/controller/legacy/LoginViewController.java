package com.techcourse.controller.legacy;

import com.techcourse.controller.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                          .map(user -> {
                              log.info("logged in {}", user.getAccount());
                              return new ModelAndView(new JspView("redirect:/index.jsp"));
                          })
                          .orElse(new ModelAndView(new JspView("redirect:/login.jsp")));
    }
}

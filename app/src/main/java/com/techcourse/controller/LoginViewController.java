package com.techcourse.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        Optional<User> user = UserSession.getUserFrom(req.getSession());
        if (user.isPresent()) {
            log.info("logged in {}", user.get().getAccount());
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login/jsp"));
    }
}

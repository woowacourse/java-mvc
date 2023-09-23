package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

@context.org.springframework.stereotype.Controller
public class LoginViewController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @Override
    public String execute(final HttpServletRequest req,
                          final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(req.getSession())
                          .map(user -> {
                              log.info("logged in {}", user.getAccount());
                              return "redirect:/index.jsp";
                          })
                          .orElse("/login.jsp");
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLoginView(final HttpServletRequest request,
                                     final HttpServletResponse response) throws Exception {
        final String viewName = execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}

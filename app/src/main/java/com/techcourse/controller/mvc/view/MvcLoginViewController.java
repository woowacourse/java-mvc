package com.techcourse.controller.mvc.view;

import com.techcourse.controller.mvc.MvcUserSession;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class MvcLoginViewController {

    private static final Logger log = LoggerFactory.getLogger(MvcLoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String path = execute(req, res);
        return new ModelAndView(new JspView(path));
    }

    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return MvcUserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
    }
}

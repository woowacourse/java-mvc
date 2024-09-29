package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/login/view")
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getLoginView(final HttpServletRequest req) throws Exception {
        String viewName = UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");
        return new ModelAndView(new JspView(viewName));
    }
}

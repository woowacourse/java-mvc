package com.techcourse.controller.v2;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.controller.UserSession;
import com.techcourse.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @RequestMapping(value = "/login/view", method = GET)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final String viewName = loginService.loginViewName(req.getSession());
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        String viewName = loginService.findByAccount(req);
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/logout", method = GET)
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }
}

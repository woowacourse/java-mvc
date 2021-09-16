package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String LOGIN_JSP = "login.jsp";
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String REDIRECT_401_JSP = "redirect:/401.jsp";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        }
        return new ModelAndView(new JspView(LOGIN_JSP));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) {
        return InMemoryUserRepository.findByAccount(req.getParameter(ACCOUNT))
            .map(user -> {
                log.info("User : {}", user);
                return login(req, user);
            })
            .orElse(new ModelAndView(new JspView(REDIRECT_INDEX_JSP)));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter(PASSWORD))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        } else {
            return new ModelAndView(new JspView(REDIRECT_401_JSP));
        }
    }
}

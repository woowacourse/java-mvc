package com.techcourse.controller.annotation;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String HOME_PATH = "/index";
    private static final String UNAUTHORIZED_PATH = "/401";

    @RequestMapping(value = "/login", method = GET)
    public ModelAndView getLogin(HttpServletRequest request, HttpServletResponse response) {
        return UserSession.getUserFrom(request.getSession())
            .map(user -> {
                LOG.info("Login: {}", user.getAccount());
                return new ModelAndView(new JspView(REDIRECT_PREFIX + HOME_PATH));
            })
            .orElse(new ModelAndView(new JspView(request.getRequestURI())));
    }

    @RequestMapping(value = "/login", method = POST)
    public ModelAndView postLogin(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_PREFIX + HOME_PATH));
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                LOG.info("User: {}", user);
                return login(request, user);
            })
            .orElse(new ModelAndView(new JspView(UNAUTHORIZED_PATH)));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);

            return new ModelAndView(new JspView(REDIRECT_PREFIX + HOME_PATH));
        }
        return new ModelAndView(new JspView(UNAUTHORIZED_PATH));
    }
}

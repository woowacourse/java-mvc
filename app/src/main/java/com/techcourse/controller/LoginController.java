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

import static com.techcourse.controller.Page.*;

@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLogin(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                          .map(user -> {
                              LOGGER.info("logged in {}", user.getAccount());
                              return new ModelAndView(new JspView(REDIRECT_INDEX_JSP.getPath()));
                          })
                          .orElse(new ModelAndView(new JspView(LOGIN_JSP.getPath())));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP.getPath()));
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                                     .map(user -> {
                                         LOGGER.info("User : {}", user);
                                         return new ModelAndView(login(req, user));
                                     })
                                     .orElse(new ModelAndView(new JspView(REDIRECT_401_JSP.getPath())));
    }


    private JspView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new JspView(REDIRECT_INDEX_JSP.getPath());
        } else {
            return new JspView(REDIRECT_401_JSP.getPath());
        }
    }
}

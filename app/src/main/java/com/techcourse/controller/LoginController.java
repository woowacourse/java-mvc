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

    private static final String REDIRECT_INDEX = "redirect:/index.jsp";
    private static final String REDIRECT_401 = "redirect:/401.jsp";
    private static final String LOGIN = "/login.jsp";

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX));
        }

        return new ModelAndView(new JspView(LOGIN));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX));
        }

        String viewName =  InMemoryUserRepository.findByAccount(request.getParameter("account"))
             .map(user -> {
                 log.info("User : {}", user);
                 return login(request, user);
             })
             .orElse(REDIRECT_401);

        return new ModelAndView(new JspView(viewName));

    }

    private String login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX;
        } else {
            return REDIRECT_401;
        }
    }
}

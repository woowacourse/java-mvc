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
public class LogInOutController {

    private static final Logger LOG = LoggerFactory.getLogger(LogInOutController.class);
    private static final String REDIRECT_INDEX = "redirect:/index.jsp";
    private static final String REDIRECT_401 = "redirect:/401.jsp";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView getLoginPage(HttpServletRequest request, HttpServletResponse response) {
        String viewName = UserSession.getUserFrom(request.getSession())
            .map(user -> {
                LOG.info("logged in {}", user.getAccount());
                return REDIRECT_INDEX;
            })
            .orElse("/login.jsp");
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX));
        }

        String viewName = InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                LOG.info("User : {}", user);
                return checkedLogin(request, user);
            })
            .orElse(REDIRECT_401);
        return new ModelAndView(new JspView(viewName));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        final HttpSession session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    private String checkedLogin(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX;
        }
        return REDIRECT_401;
    }


}

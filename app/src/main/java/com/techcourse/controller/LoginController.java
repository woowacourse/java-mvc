package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public final class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("로그인 {}", user.getAccount());
                    return new ModelAndView(new JspView("redirect:/index.jsp"));
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        final HttpSession session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    private ModelAndView login(
            final HttpServletRequest request,
            final User user
    ) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}

package com.techcourse.controller;

import com.techcourse.LogUtil;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.requestInfo("/login", RequestMethod.GET);
        return UserSession.getUserFrom(request.getSession())
            .map(user -> {
                LogUtil.info("logged in {}", user.getAccount());
                return new ModelAndView(new JspView("redirect:/index.jsp"));
            })
            .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {

        if (UserSession.isLoggedIn(request.getSession())) {
            LogUtil.info("User is logged in");
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
            .map(user -> {
                LogUtil.info("User : {}", user);
                return new ModelAndView(new JspView(login(request, user)));
            })
            .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        } else {
            return "redirect:/401.jsp";
        }
    }
}

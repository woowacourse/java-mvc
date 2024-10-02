package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request) {
        if (UserSession.isLoggedIn(request.getSession())) {
            View view = new JspView("redirect:/index.jsp");
            return new ModelAndView(view);
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(request, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            View view = new JspView("redirect:/index.jsp");
            return new ModelAndView(view);
        }
        View view = new JspView("redirect:/401.jsp");
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView render(HttpServletRequest request) {
        View indexView = new JspView("redirect:/index.jsp");
        View loginView = new JspView("/login.jsp");
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(indexView);
                })
                .orElse(new ModelAndView(loginView));
    }
}

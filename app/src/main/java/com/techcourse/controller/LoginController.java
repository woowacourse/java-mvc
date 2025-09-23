package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.Account;
import com.techcourse.domain.Password;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/api/user/login", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse res) throws Exception {
        return UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user);
                    return new ModelAndView(new JspView("redirect:/index.jsp"));
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        String value = request.getParameter("account");
        Account account = new Account(value);
        return InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("User : {}", user);
                    return authenticate(user, request);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView authenticate(final User user, final HttpServletRequest request) {
        String value = request.getParameter("password");
        Password password = new Password(value);
        if (user.checkPassword(password)) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("success", false);
        modelAndView.addObject("message", "비밀번호가 일치하지 않습니다.");
        return modelAndView;
    }
}

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
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if (UserSession.isLoggedIn(session)) {
            return new ModelAndView(new JspView("redirect:/"));
        }

        return InMemoryUserRepository.findByAccount(account)
                .map(user -> {
                    log.info("User : {}", user);
                    return authenticate(session, user, password);
                })
                .orElse(new ModelAndView(new JspView("redirect:/404.jsp")));
    }

    private ModelAndView authenticate(HttpSession session, User user, String password) {
        if (user.checkPassword(password)) {
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("redirect:/"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }
}

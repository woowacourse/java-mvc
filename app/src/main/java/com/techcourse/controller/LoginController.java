package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.interface21.webmvc.servlet.view.RedirectView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView showLoginForm(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            log.info("이미 로그인 되었으므로 리다이렉트 합니다.");
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new RedirectView("/index.jsp"));
        }
        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    if (user.checkPassword(request.getParameter("password"))) {
                        request.getSession().setAttribute(UserSession.SESSION_KEY, user);
                        return new ModelAndView(new RedirectView("/index.jsp"));
                    }
                    return new ModelAndView(new RedirectView("/401.jsp"));
                })
                .orElse(new ModelAndView(new RedirectView("/401.jsp")));
    }
}

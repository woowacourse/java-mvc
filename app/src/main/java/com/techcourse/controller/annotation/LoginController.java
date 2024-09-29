package com.techcourse.controller.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView("/index.jsp"));
        }

        return InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return redirectToHomeIfPasswordCorrect(request, user);
                })
                .orElse(new ModelAndView(new JspView("/401.jsp")));
    }

    private ModelAndView redirectToHomeIfPasswordCorrect(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView("/index.jsp"));
        }
        return new ModelAndView(new JspView("/401.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView showPage(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/login.jsp"));
    }
}

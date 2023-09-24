package com.techcourse.controller.annotaion;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class LoginController {

    Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return modelAndJspViewFor("redirect:index.jsp");
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ModelAndView handleLogin(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return modelAndJspViewFor("redirect:index.jsp");
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return login(req, user);
            })
            .orElseGet(() -> modelAndJspViewFor("redirect:/401.jsp"));
    }

    private ModelAndView login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return modelAndJspViewFor("redirect:/index.jsp");
        }
        return modelAndJspViewFor("redirect:/401.jsp");
    }

    private ModelAndView modelAndJspViewFor(String path) {
        return new ModelAndView(new JspView(path));
    }
}

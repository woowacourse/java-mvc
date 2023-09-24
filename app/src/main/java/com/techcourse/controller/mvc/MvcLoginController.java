package com.techcourse.controller.mvc;

import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class MvcLoginController {

    private static final Logger log = LoggerFactory.getLogger(MvcLoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView(
                InMemoryUserRepository.findByAccount(req.getParameter("account"))
                        .map(user -> {
                            log.info("User : {}", user);
                            return login(req, user);
                        })
                        .orElse("redirect:/401.jsp")
        ));
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView viewLogin(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView(
                UserSession.getUserFrom(req.getSession())
                        .map(user -> {
                            log.info("logged in {}", user.getAccount());
                            return "redirect:/index.jsp";
                        })
                        .orElse("/login.jsp")
        ));
    }
}

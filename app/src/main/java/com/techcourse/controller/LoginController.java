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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }

        String page = InMemoryUserRepository.findByAccount(req.getParameter("account"))
            .map(user -> {
                log.info("User : {}", user);
                return authenticate(req, user);
            })
            .orElse("redirect:/401.jsp");

        return new ModelAndView(new JspView(page));
    }

    private String authenticate(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String page = UserSession.getUserFrom(req.getSession())
            .map(user -> {
                log.info("logged in {}", user.getAccount());
                return "redirect:/index.jsp";
            })
            .orElse("/login.jsp");

        return new ModelAndView(new JspView(page));
    }
}

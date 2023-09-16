package com.techcourse.controller.annotation;

import com.techcourse.controller.LoginController;
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
public class AnnotationLoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_401_JSP = "redirect:/401.jsp";
    private static final String LOGIN_JSP = "/login.jsp";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView display(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
                })
                .orElse(new ModelAndView(new JspView(LOGIN_JSP)));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        }

        ModelAndView modelAndView = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return checkPassword(req, user);
                })
                .orElse(new ModelAndView(new JspView(REDIRECT_401_JSP)));
        modelAndView.addObject("id", req.getAttribute("id"));
        return modelAndView;
    }

    private ModelAndView checkPassword(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        }
        return new ModelAndView(new JspView(REDIRECT_401_JSP));
    }
}

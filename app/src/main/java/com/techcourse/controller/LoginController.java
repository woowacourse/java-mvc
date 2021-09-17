package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private static final String REDIRECT_VIEW = "redirect:/index.jsp";

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(new JspView(REDIRECT_VIEW));
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView execute(HttpServletRequest req, HttpServletResponse res) {
        ModelAndView modelAndView;
        if (UserSession.isLoggedIn(req.getSession())) {
            modelAndView = new ModelAndView(new JspView(REDIRECT_VIEW));
            return modelAndView;
        }

        modelAndView = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));

        return modelAndView;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        return new ModelAndView(new JspView("redirect:/"));
    }

    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return new ModelAndView(new JspView(REDIRECT_VIEW));
        } else {
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
    }
}

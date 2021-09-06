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
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            final JspView jspView = new JspView("redirect:/index.jsp");
            new ModelAndView(jspView);
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) throws Exception {
        final HttpSession session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        final JspView jspView = new JspView("redirect:/");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest req, HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    final JspView jspView = new JspView("redirect:/index.jsp");
                    return new ModelAndView(jspView);
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        final JspView jspView = new JspView("redirect:/index.jsp");
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView registerView(HttpServletRequest req, HttpServletResponse res) throws Exception {
        final JspView jspView = new JspView("/register.jsp");
        return new ModelAndView(jspView);
    }


    private ModelAndView login(HttpServletRequest request, User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final HttpSession session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            final JspView jspView = new JspView("redirect:/index.jsp");
            return new ModelAndView(jspView);
        } else {
            final JspView jspView = new JspView("redirect:/401.jsp");
            return new ModelAndView(jspView);
        }
    }

}

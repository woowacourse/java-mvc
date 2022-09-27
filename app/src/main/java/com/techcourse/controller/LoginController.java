package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_401_JSP = "redirect:/401.jsp";

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        return UserSession.getUserFrom(req.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
                })
                .orElse(new ModelAndView(new JspView("/login.jsp")));
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public ModelAndView login(final HttpServletRequest req, final HttpServletResponse res) {
        if (UserSession.isLoggedIn(req.getSession())) {
            return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
        }

        final String uri = InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return login(req, user);
                })
                .orElse(REDIRECT_401_JSP);

        return new ModelAndView(new JspView(uri));
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public ModelAndView logout(final HttpServletRequest req, final HttpServletResponse res) {
        final var session = req.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return REDIRECT_INDEX_JSP;
        } else {
            return REDIRECT_401_JSP;
        }
    }
}

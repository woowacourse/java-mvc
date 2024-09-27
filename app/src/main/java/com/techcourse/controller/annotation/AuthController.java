package com.techcourse.controller.annotation;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = UserSession.getUserFrom(request.getSession())
                .map(user -> {
                    log.info("logged in {}", user.getAccount());
                    return "redirect:/index.jsp";
                })
                .orElse("/login.jsp");

        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (UserSession.isLoggedIn(request.getSession())) {
            View view = new JspView("redirect:/index.jsp");
            return new ModelAndView(view);
        }

        String viewName = InMemoryUserRepository.findByAccount(request.getParameter("account"))
                .map(user -> login(request, user))
                .orElse("redirect:/401.jsp");

        View view = new JspView(viewName);
        return new ModelAndView(view);
    }

    private String login(final HttpServletRequest request, final User user) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            return "redirect:/index.jsp";
        }
        return "redirect:/401.jsp";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final var session = request.getSession();
        session.removeAttribute(UserSession.SESSION_KEY);
        JspView view = new JspView("redirect:/");
        return new ModelAndView(view);
    }
}

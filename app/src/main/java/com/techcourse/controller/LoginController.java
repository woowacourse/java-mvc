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
    public ModelAndView login(
            final HttpServletRequest req,
            final HttpServletResponse res
    ) throws Exception {
        if (UserSession.isLoggedIn(req.getSession())) {
            final JspView jspView = new JspView("redirect:/index.jsp");

            return new ModelAndView(jspView);
        }

        return InMemoryUserRepository.findByAccount(req.getParameter("account"))
                .map(user -> {
                    log.info("User : {}", user);
                    return doLogin(req, user);
                })
                .orElse(new ModelAndView(new JspView("redirect:/401.jsp")));
    }

    private ModelAndView doLogin(
            final HttpServletRequest request,
            final User user
    ) {
        if (user.checkPassword(request.getParameter("password"))) {
            final var session = request.getSession();
            session.setAttribute(UserSession.SESSION_KEY, user);
            final JspView jspView = new JspView("redirect:/index.jsp");

            return new ModelAndView(jspView);
        }
        final JspView jspView = new JspView("redirect:/401.jsp");

        return new ModelAndView(jspView);
    }
}

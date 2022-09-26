package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@nextstep.web.annotation.Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLoginPage(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/index.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(final HttpServletRequest request, final HttpServletResponse res) {
        final var user = findUser(request);
        if (user.isPresent()) {
            if (user.get().checkPassword(request.getParameter("password"))) {
                final var session = request.getSession();
                session.setAttribute(UserSession.SESSION_KEY, user);
                return new ModelAndView(new JspView("redirect:/index.jsp"));
            }
            return new ModelAndView(new JspView("redirect:/401.jsp"));
        }
        return new ModelAndView(new JspView("redirect:/401.jsp"));
    }

    private Optional<User> findUser(final HttpServletRequest request) {
        return InMemoryUserRepository.findByAccount(request.getParameter("account"));
    }
}

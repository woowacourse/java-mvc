package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AnnotationLogoutController {

    private static final Logger log = LoggerFactory.getLogger(AnnotationLogoutController.class);

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
        final HttpSession session = req.getSession();

        Optional<User> user = UserSession.getUserFrom(session);
        user.ifPresent(value -> log.info("logged out {}", value.getAccount()));
        session.removeAttribute(UserSession.SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }
}

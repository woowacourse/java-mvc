package com.techcourse.controller;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginAnnotationController {
    private static final Logger log = LoggerFactory.getLogger(LoginAnnotationController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginPage(final HttpServletRequest req, final HttpServletResponse res) {
        Optional<User> userFrom = UserSession.getUserFrom(req.getSession());
        if (userFrom.isPresent()) {
            log.info("logged in {}", userFrom.get().getAccount());
            return new ModelAndView(new JspView("redirect:/index.jsp"));
        }
        return new ModelAndView(new JspView("/login.jsp"));
    }
}

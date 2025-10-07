package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginViewController {

    private static final Logger log = LoggerFactory.getLogger(LoginViewController.class);

    @RequestMapping(value = "/login/view", method = RequestMethod.GET)
    public ModelAndView loginView(final HttpServletRequest req, final HttpServletResponse res) {
        if (isLoggedIn(req)) {
            return redirectToIndex();
        }

        return redirectToLoginView();
    }

    private boolean isLoggedIn(final HttpServletRequest req) {
        final Optional<User> user = UserSession.getUserFrom(req.getSession());
        if (user.isEmpty()) {
            return false;
        }

        log.info("이미 로그인 상태입니다. User: {}", user.get().getAccount());
        return true;
    }

    private ModelAndView redirectToIndex() {
        final String viewName = "redirect:/index.jsp";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }

    private ModelAndView redirectToLoginView() {
        final String viewName = "/login.jsp";
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}

package com.techcourse.controller;

import com.techcourse.Pages;
import com.techcourse.domain.User;
import com.techcourse.domain.UserSession;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Autowired;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest request, HttpServletResponse response) {
        final User user = new User(
            request.getParameter("account"),
            request.getParameter("password"),
            request.getParameter("email"));

        userService.save(user);

        return new ModelAndView(new JspView(Pages.INDEX.redirectPageName()));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView getRegisterPage(HttpServletRequest request, HttpServletResponse response) {
        if (UserSession.isLoggedIn(request.getSession())) {
            return new ModelAndView(new JspView(Pages.INDEX.redirectPageName()));
        }
        return new ModelAndView(new JspView(Pages.REGISTER.getPageName()));
    }
}

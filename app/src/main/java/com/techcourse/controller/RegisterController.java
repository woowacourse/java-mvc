package com.techcourse.controller;

import com.techcourse.Pages;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(HttpServletRequest req, HttpServletResponse res) {
        // todo - service로 분리
        final User user = new User(2,
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView(Pages.INDEX.redirectPageName()));
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView getRegisterPage(HttpServletRequest req, HttpServletResponse res) {
        // todo - 로그인이면 차단
        return new ModelAndView(new JspView(Pages.REGISTER.getPageName()));
    }
}

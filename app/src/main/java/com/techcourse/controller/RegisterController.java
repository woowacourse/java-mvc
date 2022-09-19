package com.techcourse.controller;

import com.techcourse.LogUtil;
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

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView registerView(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.requestInfo("/register/view", RequestMethod.GET);
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.requestInfo("/register", RequestMethod.POST);
        final String account = request.getParameter("account");
        final String password = request.getParameter("password");
        final String email = request.getParameter("email");
        final var user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        LogUtil.info("New User Registered. account: {}, password: {}, email: {}", account, password, email);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

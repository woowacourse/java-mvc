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

@Controller
public class RegisterController {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        final var user = new User(2,
                request.getParameter(ACCOUNT),
                request.getParameter(PASSWORD),
                request.getParameter(EMAIL));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView("login.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView view(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView("register.jsp"));
    }
}

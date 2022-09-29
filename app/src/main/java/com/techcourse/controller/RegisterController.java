package com.techcourse.controller;

import java.util.Objects;
import java.util.stream.Stream;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method= RequestMethod.GET)
    public ModelAndView showView(final HttpServletRequest req, final HttpServletResponse res) {
        return Page.REGISTER.getModelAndView();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(final HttpServletRequest req, final HttpServletResponse res) {
        final var account = req.getParameter("account");
        final var password = req.getParameter("password");
        final var email = req.getParameter("email");

        if (isNullAnyParameter(account, password)) {
            return Page.ERROR_404.getModelAndView();
        }

        final var user = new User(2, account, password, email);
        InMemoryUserRepository.save(user);

        return Page.INDEX.getRedirectModelAndView();
    }

    private boolean isNullAnyParameter(final String... parameters) {
        return Stream.of(parameters)
                .anyMatch(Objects::isNull);
    }
}

package com.techcourse.controller;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        final String account = req.getParameter("account");

        if (isNullAnyParameter(account)) {
            return Page.ERROR_404.getModelAndView();
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final Optional<User> user = InMemoryUserRepository.findByAccount(account);

        if (user.isEmpty()) {
            modelAndView.addObject("user", null);
            return modelAndView;
        }

        modelAndView.addObject("user", user.get());
        return modelAndView;
    }

    private boolean isNullAnyParameter(final String... parameters) {
        return Stream.of(parameters)
                .anyMatch(Objects::isNull);
    }
}

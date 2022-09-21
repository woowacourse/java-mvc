package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Stream;
import nextstep.mvc.controller.asis.Controller;

public class RegisterController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (absenceEssential(req)) {
            return "redirect:/register.jsp";
        }
        registerUser(req);

        return "redirect:/index.jsp";
    }

    private boolean absenceEssential(final HttpServletRequest req) {
        final var account = req.getParameter("account");
        final var password = req.getParameter("password");
        final var email = req.getParameter("email");

        return Stream.of(account, password, email)
                .anyMatch(Objects::isNull);
    }

    private static void registerUser(final HttpServletRequest req) {
        final var user = new User(
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email")
        );

        InMemoryUserRepository.save(user);
    }
}

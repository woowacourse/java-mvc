package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Stream;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public String registerView(final HttpServletRequest req, final HttpServletResponse res) {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(final HttpServletRequest req, final HttpServletResponse res) {
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

    private void registerUser(final HttpServletRequest req) {
        final var user = new User(
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email")
        );

        InMemoryUserRepository.save(user);

        log.info("New User Registered : {} : {}", user.getAccount(), user.getEmail());
    }
}

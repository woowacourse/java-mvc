package com.techcourse.controller.v1;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handler.asis.Controller;

public class RegisterV1Controller implements Controller {

    private final InMemoryUserRepository userRepository;

    public RegisterV1Controller(final InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        userRepository.save(user);

        return "redirect:/index.jsp";
    }
}

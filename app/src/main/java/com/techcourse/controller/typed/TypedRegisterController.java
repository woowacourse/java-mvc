package com.techcourse.controller.typed;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class TypedRegisterController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return "redirect:/index.jsp";
    }
}

package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.DuplicatedUserException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

public class RegisterController implements Controller {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        try {
            InMemoryUserRepository.save(user);
        } catch (DuplicatedUserException e) {
            res.setStatus(409);
            return "409.jsp";
        }
        return "redirect:/index.jsp";
    }
}

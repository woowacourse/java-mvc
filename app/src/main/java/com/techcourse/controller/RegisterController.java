package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String show() {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String save(final HttpServletRequest req) {
        final var user = new User(
                2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email")
        );
        InMemoryUserRepository.save(user);

        return "redirect:/index.jsp";
    }
}

package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterController {

    private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public Object show(HttpServletRequest req, HttpServletResponse res) {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        log.info("register {}", user);

        return "redirect:/index.jsp";
    }
}

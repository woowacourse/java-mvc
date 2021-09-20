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
    private static final String REGISTER_VIEW = "/register.jsp";
    private static final String REDIRECT_INDEX_VIEW = "redirect:/index.jsp";

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String show(HttpServletRequest request, HttpServletResponse response) {
        return REGISTER_VIEW;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        final String password = request.getParameter("password");
        final String email = request.getParameter("email");

        if (account.isBlank() || password.isBlank() || email.isBlank()) {
            log.debug("User register fail!");
            return "redirect:" + REGISTER_VIEW;
        }

        User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        return REDIRECT_INDEX_VIEW;
    }
}

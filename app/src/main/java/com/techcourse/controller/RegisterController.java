package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REGISTER_JSP = "register.jsp";

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String save(HttpServletRequest req, HttpServletResponse res) {
        final User user = new User(
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return REDIRECT_INDEX_JSP;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String show(HttpServletRequest req, HttpServletResponse res) {
        return REGISTER_JSP;
    }
}

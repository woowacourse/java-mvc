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
    private Long id = 2L;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(id++,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);
        log.info("user = [ id: " + user.getId() + ", account: " + user.getAccount() + "]");
        return "redirect:/index.jsp";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String findRegisterView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}

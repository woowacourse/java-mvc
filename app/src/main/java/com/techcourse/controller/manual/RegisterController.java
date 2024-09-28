package com.techcourse.controller.manual;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.domain.User;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegisterController implements Controller {

    private UserService userService = new UserService();

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        userService.register(user);

        return "redirect:/index.jsp";
    }
}

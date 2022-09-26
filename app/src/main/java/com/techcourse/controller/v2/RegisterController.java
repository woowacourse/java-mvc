package com.techcourse.controller.v2;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@nextstep.web.annotation.Controller
public class RegisterController {

    @RequestMapping(value = "/v2/register", method = RequestMethod.POST)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return "redirect:/index.jsp";
    }
}

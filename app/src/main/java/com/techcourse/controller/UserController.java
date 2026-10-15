package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        if (account == null || account.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ModelAndView(new JsonView()).addObject("user", null);
        }

        final var user = InMemoryUserRepository.findByAccount(account).orElse(null);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return new ModelAndView(new JsonView()).addObject("user", user);
    }
}

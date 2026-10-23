package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@Controller
public class UserController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) {
        final var account = req.getParameter("account");
        if (account == null || account.isBlank()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ModelAndView(new JsonView()).addObject("error", Map.of("message", "account is required"));
        }

        final var user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ModelAndView(new JsonView()).addObject("error", Map.of("message", "user not found"));
        }

        return new ModelAndView(new JsonView()).addObject("user", Map.of("account", user.get().getAccount()));
    }
}

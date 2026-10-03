package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        if (account == null || account.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return modelAndView.addObject("error", "account is required");
        }

        final Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return modelAndView.addObject("error", "User not found");
        }

        modelAndView.addObject("user", user.get());
        return modelAndView;
    }
}

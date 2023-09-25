package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.NotExistUserException;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

@Controller
public class UserController {

    private static final String ACCOUNT = "account";
    private static final String USER = "user";

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse ignored) {
        final String account = request.getParameter(ACCOUNT);
        if (validateNotExistUser(account)) {
            throw new NotExistUserException("[ERROR] 해당 유저가 존재하지 않습니다.");
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new NotExistUserException("[ERROR] 해당 유저가 존재하지 않습니다."));

        modelAndView.addObject(USER, user);
        return modelAndView;
    }

    private boolean validateNotExistUser(final String account) {
        return Objects.isNull(account) || notExistUser(account);
    }

    private Boolean notExistUser(final String account) {
        return InMemoryUserRepository.findByAccount(account).isEmpty();
    }
}

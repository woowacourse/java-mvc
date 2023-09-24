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

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        if (validateNotExistUser(account)) {
            return NotExistUserException.createJsonMessage();
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 예상하지 못한 에러가 발생했습니다."));

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    private boolean validateNotExistUser(final String account) {
        return Objects.isNull(account) || notExistUser(account);
    }

    private Boolean notExistUser(final String account) {
        return InMemoryUserRepository.findByAccount(account).isEmpty();
    }
}

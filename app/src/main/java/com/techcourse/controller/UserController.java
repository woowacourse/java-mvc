package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request, final HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);
        if (account == null) {
            return ModelAndView.redirectTo("/404.jsp");
        }

        User user = null;
        try {
            log.debug("userRepository.findByAccount() : {}", account);
            user = InMemoryUserRepository.findByAccount(account)
                    .orElseThrow(() -> new IllegalArgumentException("account에 해당하는 user를 찾을 수가 없습니다."));
        } catch (final IllegalArgumentException e) {
            return ModelAndView.redirectTo("/400.jsp");
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}

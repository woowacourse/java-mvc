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
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JsonView;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse ignored) {
        final String account = request.getParameter("account");

        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());

        if (account == null) {
            modelAndView.addObject("message", "account를 입력해주세요.");
            return modelAndView;
        }

        final User user = InMemoryUserRepository.findByAccount(account)
                                                .orElseThrow(() -> new IllegalArgumentException("존재하는 user가 아닙니다."));

        modelAndView.addObject("user", user);
        return modelAndView;
    }
}

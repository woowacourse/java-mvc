package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.dto.AddUserDto;
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
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow();

        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, AddUserDto userRequest) {
        log.info("add called");
        log.info("userRequest info : {} ", userRequest);
        
        final var user = new User(userRequest.getAccount(), userRequest.getPassword(), userRequest.getEmail());
        InMemoryUserRepository.save(user);

        response.setStatus(201);
        return new ModelAndView(new JsonView());
    }

}

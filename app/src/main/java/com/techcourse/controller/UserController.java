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

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user/empty", method = RequestMethod.GET)
    public ModelAndView emptyUser(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JsonView());
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView oneUser(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final User user = InMemoryUserRepository.findByAccount(account).orElseThrow();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView allUsers(HttpServletRequest request, HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        var users = InMemoryUserRepository.findAll();
        for (User user : users) {
            modelAndView.addObject("user", user);
        }
        return modelAndView;
    }
}

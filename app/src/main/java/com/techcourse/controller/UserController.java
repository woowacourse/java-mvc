package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        final User user = userService.findByAccount(request.getParameter("account"));

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ModelAndView showAll(HttpServletRequest request, HttpServletResponse response) {
        log.info("Method: GET, Request URI: {}", request.getRequestURI());

        final List<User> users = userService.findByAccounts(
            request.getParameterValues("account")
        );

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        for (User user : users) {
            modelAndView.addObject("users", user);
        }
        return modelAndView;
    }
}


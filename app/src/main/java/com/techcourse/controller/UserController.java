package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UserNotFoundException;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("account");
        LOG.debug("user account : {}", account);

        try {
            User user = userService.findUserByAccount(account);

            ModelAndView modelAndView = new ModelAndView(new JsonView());
            modelAndView.addObject("user", user);

            return modelAndView;
        } catch (UserNotFoundException e) {
            LOG.debug("Not found User by Account: {}", account);

            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }
    }
}


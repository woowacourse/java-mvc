package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");

        LOG.info("user id : {}", account);

        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", new User(0L, account, "password", "mungto@gmail.com"));
        return modelAndView;
    }

    @RequestMapping(value = "/api/multiUser", method = RequestMethod.GET)
    public ModelAndView multiUser(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("mungto", new User(0L, "mungto", "password", "mungto@gmail.com"));
        modelAndView.addObject("gugu", InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        return modelAndView;
    }
}

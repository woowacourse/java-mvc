package com.techcourse.controller;

import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Autowired;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private static final String MUNGTO = "mungto";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");

        LOG.info("user id : {}", account);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userService.findById(account));
        return modelAndView;
    }

    @RequestMapping(value = "/api/multiUser", method = RequestMethod.GET)
    public ModelAndView multiUser(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MUNGTO, userService.findById(MUNGTO))
            .addObject("gugu", userService.findById("gugu"));
        return modelAndView;
    }
}

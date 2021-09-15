package com.techcourse.air.controller;

import com.techcourse.air.core.annotation.Controller;
import com.techcourse.air.domain.User;
import com.techcourse.air.mvc.core.view.JsonView;
import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import com.techcourse.air.mvc.web.support.RequestMethod;
import com.techcourse.air.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}

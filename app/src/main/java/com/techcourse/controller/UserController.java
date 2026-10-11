package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request,
                             final HttpServletResponse response) {
        final String account = request.getParameter("account");

        log.debug("user id : {}", account);

        final var user = InMemoryUserRepository
                            .findByAccount(account).orElseThrow();

        // JSP가 아닌 JsonView를 사용한다.
        final ModelAndView modelAndView = new ModelAndView(new JsonView());

        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
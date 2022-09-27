package com.techcourse.controller;

import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final var account = request.getParameter("account");
        log.debug("user id : {}", account);
        if (validate(account)) {
            return new ModelAndView(new JspView("/404.jsp"));
        }

        final var modelAndView = new ModelAndView(new JsonView());
        final var foundUser = InMemoryUserRepository.findByAccount(account);
        if (foundUser.isEmpty()) {
            return new ModelAndView(new JspView("/404.jsp"));
        }

        modelAndView.addObject("user", foundUser.get());
        return modelAndView;
    }

    private boolean validate(final String account) {
        return Objects.isNull(account);
    }
}


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
    private static final String PARAM_NAME_ACCOUNT = "account";

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView getUser(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidRequest(request)) {
            throw new IllegalArgumentException("Please input account as parameter");
        }

        final String account = request.getParameter(PARAM_NAME_ACCOUNT);
        log.debug("user id : {}", account);

        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + account));

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    private boolean isValidRequest(HttpServletRequest request) {
        String account = request.getParameter(PARAM_NAME_ACCOUNT);
        return account != null && !account.isBlank();
    }
}

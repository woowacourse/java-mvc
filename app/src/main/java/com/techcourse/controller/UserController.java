package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import com.techcourse.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
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

    private final UserRepository userRepository;

    public UserController() {
        this.userRepository = new InMemoryUserRepository();
    }

    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        final String account = request.getParameter("account");
        if (account == null) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }
        log.debug("user id : {}", account);

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final Optional<User> maybeUser = userRepository.findByAccount(account);

        if (maybeUser.isEmpty()) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }

        modelAndView.addObject("user", maybeUser.get());
        return modelAndView;
    }
}

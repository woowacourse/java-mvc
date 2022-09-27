package com.techcourse.controller.v2.controller;

import static nextstep.web.support.RequestMethod.GET;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;

@Controller
public class RegisterController {

    private final InMemoryUserRepository userRepository;

    public RegisterController(final InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/register/view", method = GET)
    public ModelAndView registerView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView register(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        userRepository.save(user);

        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

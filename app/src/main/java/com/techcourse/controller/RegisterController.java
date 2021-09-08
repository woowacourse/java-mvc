package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.exception.UnauthorizedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestParam;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam String account, @RequestParam String password, @RequestParam String email) {
        final User user = new User(2, account, password, email);
        InMemoryUserRepository.save(user);

        return new ModelAndView("redirect:/index.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("/register.jsp");
    }
}

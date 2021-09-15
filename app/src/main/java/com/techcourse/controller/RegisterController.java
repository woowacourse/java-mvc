package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return ModelAndView.ofJsp("/register.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        User user = new User(2,
            req.getParameter("account"),
            req.getParameter("password"),
            req.getParameter("email"));
        InMemoryUserRepository.save(user);

        return ModelAndView.ofJsp("redirect:/index.jsp");
    }

}

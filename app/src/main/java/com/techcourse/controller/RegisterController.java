package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller("/register")
public class RegisterController {

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView viewLogoutPage(HttpServletRequest request, HttpServletResponse response) {
        return ModelAndView.createWithViewName("/register.jsp");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        final User user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return ModelAndView.createWithViewName("redirect:/index.jsp");
    }
}

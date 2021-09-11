package com.techcourse.controller;

import com.techcourse.repository.InMemoryUserRepository;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.annotation.RequestParam;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             @RequestParam("email") String email) {
        InMemoryUserRepository.save(account, password, email);

        return new ModelAndView("redirect:/index.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show() {
        return new ModelAndView("/register.jsp");
    }
}

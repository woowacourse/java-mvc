package com.techcourse.controller.rest;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserRestController {

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("account");
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        InMemoryUserRepository.findByAccount(account)
                .ifPresentOrElse(
                        user -> modelAndView.addObject("user", user),
                        () -> modelAndView.addObject("message", "사용자를 찾을 수 없습니다.")
                );

        return modelAndView;
    }

    @RequestMapping(value = "/api/multiple", method = RequestMethod.GET)
    public ModelAndView showMultiple(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView(new JsonView());

        User user1 = new User(2, "테바오", "1234", "tebah@a.com");
        User user2 = new User(3, "트레비", "1234", "tre@a.com");

        modelAndView.addObject("user1", user1);
        modelAndView.addObject("user2", user2);

        return modelAndView;
    }
}

package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController  {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);
        View view = new JspView("redirect:/index.jsp");
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        View view = new JspView("/register.jsp");
        return new ModelAndView(view);
    }
}

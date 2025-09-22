package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView getRegisterView(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView(new JspView("/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView("redirect:/register.jsp"));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
        if (req.getParameter("account") == null ||
                req.getParameter("password") == null ||
                req.getParameter("email") == null
        ) {
            return new ModelAndView(new JspView("redirect:/404.jsp"));
        }

        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

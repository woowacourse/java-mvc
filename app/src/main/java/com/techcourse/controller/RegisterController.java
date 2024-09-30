package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

@Controller
public class RegisterController {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REDIRECT_400_JSP = "redirect:/400.jsp";
    private static final String REGISTER_JSP = "/register.jsp";

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView(new JspView(REGISTER_JSP));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest req, HttpServletResponse res) {
        if (req.getParameter(ACCOUNT) == null ||
                req.getParameter(PASSWORD) == null || req.getParameter(EMAIL) == null) {
            return new ModelAndView(new JspView(REDIRECT_400_JSP));
        }

        final var user = new User(2,
                req.getParameter(ACCOUNT),
                req.getParameter(PASSWORD),
                req.getParameter(EMAIL));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView(REDIRECT_INDEX_JSP));
    }
}

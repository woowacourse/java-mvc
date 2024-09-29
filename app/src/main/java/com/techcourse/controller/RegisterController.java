package com.techcourse.controller;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@com.interface21.context.stereotype.Controller
public class RegisterController {

    private static final String REGISTER_JSP = "/register.jsp";

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerPage(final HttpServletRequest req, HttpServletResponse res) {
        JspView jspView = new JspView(REGISTER_JSP);
        return new ModelAndView(jspView);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(final HttpServletRequest req, final HttpServletResponse res) {
        final var user = new User(2,
                req.getParameter("account"),
                req.getParameter("password"),
                req.getParameter("email"));
        InMemoryUserRepository.save(user);

        View view = new JspView("redirect:/index.jsp");
        return new ModelAndView(view);
    }
}

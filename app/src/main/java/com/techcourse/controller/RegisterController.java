package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";
    private static final String REGISTER_JSP = "/register.jsp";

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        User user = new User(
                2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email")
        );
        InMemoryUserRepository.save(user);

        JspView view = new JspView(REDIRECT_INDEX_JSP);
        return new ModelAndView(view);
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        JspView view = new JspView(REGISTER_JSP);
        return new ModelAndView(view);
    }
}

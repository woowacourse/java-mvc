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

    private static final String REGISTER_JSP = "/register.jsp";
    private static final String REDIRECT_INDEX_JSP = "redirect:/index.jsp";

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView getRegister(HttpServletRequest request, HttpServletResponse response) {
        return convertStringToMav(REGISTER_JSP);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView postRegister(HttpServletRequest request, HttpServletResponse response) {
        final var user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return convertStringToMav(REDIRECT_INDEX_JSP);
    }

    private ModelAndView convertStringToMav(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}

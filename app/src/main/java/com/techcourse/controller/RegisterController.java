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
import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        HttpSession session = request.getSession();

        User user = new User(2, account, password, email);
        InMemoryUserRepository.save(user);
        session.setAttribute(UserSession.SESSION_KEY, user);

        return new ModelAndView(new JspView("redirect:/"));
    }
}

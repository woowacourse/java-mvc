package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.domain.UserCreationException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, final HttpServletResponse response) {
        try {
            User user = new User(2,
                    request.getParameter("account"),
                    request.getParameter("password"),
                    request.getParameter("email"));
            System.out.println(user);

            InMemoryUserRepository.save(user);
            return responsePage("redirect:/index.jsp");

        } catch (UserCreationException e) {
            return responsePage("/register.jsp");
        }
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView registerPage(HttpServletRequest request, final HttpServletResponse response) {
        return responsePage("/register.jsp");
    }

    private ModelAndView responsePage(String viewName) {
        return new ModelAndView(new JspView(viewName));
    }
}

package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspViewRenderer;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController implements JspViewRenderer {

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerView(final HttpServletRequest request, final HttpServletResponse response) {
        return showPage("/register.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView registerUser(final HttpServletRequest request, final HttpServletResponse response) {
        final var user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return redirect("/index.jsp");
    }
}

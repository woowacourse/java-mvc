package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class RegisterController {

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView signUp(final HttpServletRequest request,
                              final HttpServletResponse response) {
        String account = request.getParameter("account");
        Optional<User> user = InMemoryUserRepository.findByAccount(account);
        if (user.isEmpty()) {
            int id = InMemoryUserRepository.getNextUserId();
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            final User savedUser = new User(id, account, password, email);
            InMemoryUserRepository.save(savedUser);
        }
        JspView jspView = new JspView("redirect:/index.jsp");
        return new ModelAndView(jspView);
    }
}

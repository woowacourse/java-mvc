package com.techcourse.controllerv2;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

import static web.org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegisterControllerV2 {

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final User user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        InMemoryUserRepository.save(user);

        return new ModelAndView(new JspView( "redirect:/index.jsp"));
    }
}

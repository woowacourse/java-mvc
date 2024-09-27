package com.techcourse.controller.annotation;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.domain.User;
import com.techcourse.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterController {

    private final UserService userService = new UserService();

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return resolveModelAndView("/register.jsp");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final var user = new User(2,
                request.getParameter("account"),
                request.getParameter("password"),
                request.getParameter("email"));
        userService.register(user);

        return resolveModelAndView("redirect:/index.jsp");
    }

    private ModelAndView resolveModelAndView(String viewName) {
        View view = new JspView(viewName);
        return new ModelAndView(view);
    }
}

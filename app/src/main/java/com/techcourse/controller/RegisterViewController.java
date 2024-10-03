package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }
}

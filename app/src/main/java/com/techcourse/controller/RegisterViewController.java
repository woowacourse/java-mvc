package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerView() {
        return "/register.jsp";
    }
}

package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
public class RegisterViewController {

    @RequestMapping("/register/view")
    public ModelAndView registerView() {
        return new ModelAndView("/register.jsp");
    }
}

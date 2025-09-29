package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView show() {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}

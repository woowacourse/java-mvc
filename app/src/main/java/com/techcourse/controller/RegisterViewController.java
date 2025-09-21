package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterViewController {

    @RequestMapping(method = RequestMethod.GET, value = "/register")
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return new ModelAndView("/register.jsp");
    }
}

package com.techcourse.controller.legacy;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class RegisterViewController {

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res)  {
        return new ModelAndView(new JspView("/register.jsp"));
    }
}

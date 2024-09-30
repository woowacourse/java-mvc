package com.techcourse.controller;

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
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        String viewName = "/register.jsp";
        JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}

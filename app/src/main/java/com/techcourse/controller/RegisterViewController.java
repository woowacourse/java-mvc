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

    private static final String REGISTER_PATH = "/register.jsp";

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return redirect(REGISTER_PATH);
    }

    private ModelAndView redirect(String path) {
        return new ModelAndView(new JspView(path));
    }
}

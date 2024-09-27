package com.techcourse.controller.manual;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@com.interface21.context.stereotype.Controller
public class RegisterViewController implements Controller {

    @Override
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/register.jsp";
    }

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView registerView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JspView view = new JspView(execute(request, response));
        return new ModelAndView(view);
    }
}

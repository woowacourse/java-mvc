package com.techcourse.controller.annotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class RegisterViewController {

    private static final Logger log = LoggerFactory.getLogger(RegisterViewController.class);

    @RequestMapping(value = "/register/view", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("annotation based handler");
        return new ModelAndView(new JspView("/register.jsp"));
    }
}

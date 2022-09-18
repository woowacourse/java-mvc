package com.techcourse.controllerV2;

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
public class RootController {

    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView doGet(final HttpServletRequest req, final HttpServletResponse res) {
        return new ModelAndView(new JspView("redirect:/index.jsp"));
    }
}

package webmvc.org.springframework.web.servlet.mvc.tobe.default_controller;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

@Controller
public class ForwardController {

    private static final String ROOT_DIR = "/index.jsp";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView show(final HttpServletRequest request,
                             final HttpServletResponse response) {
        return new ModelAndView(new JspView(ROOT_DIR));
    }
}

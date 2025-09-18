package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

@Controller
public class ForwardController {

    private static final String DEFAULT_PATH = "/index.jsp";

    private final String path;

    public ForwardController(){
        this.path = DEFAULT_PATH;
    }

    public ForwardController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        return new ModelAndView(new JspView(path));
    }
}

package com.samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AnnotationController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return "/index.jsp";
    }
}

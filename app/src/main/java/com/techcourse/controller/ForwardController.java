package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ForwardController {

    private static final Logger log = LoggerFactory.getLogger(ForwardController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
            return "redirect:/index.jsp";
    }
}

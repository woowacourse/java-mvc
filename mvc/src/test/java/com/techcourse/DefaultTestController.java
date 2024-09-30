package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

@Controller
public class DefaultTestController {

    private static final Logger log = LoggerFactory.getLogger(DefaultTestController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        return "/index.jsp";
    }
}


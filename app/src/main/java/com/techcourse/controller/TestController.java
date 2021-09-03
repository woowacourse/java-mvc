package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/get-test", method = RequestMethod.GET)
    public String findUserId(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller get method");
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/post-test", method = RequestMethod.POST)
    public String save(HttpServletRequest request, HttpServletResponse response) {
        log.info("test controller post method");
        return "redirect:/index.html";
    }
}

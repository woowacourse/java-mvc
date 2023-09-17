package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

public class DefaultHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(DefaultHandlerMapping.class);

    private static final Controller notFoundController = new ForwardController("404.jsp");

    public DefaultHandlerMapping() {
    }

    public Controller getNotFoundController() {
        return notFoundController;
    }
}

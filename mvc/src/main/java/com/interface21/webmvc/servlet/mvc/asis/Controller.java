package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// TODO : ModelAndView 어댑팅
public interface Controller {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}

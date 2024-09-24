package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {

    ModelAndView handle(HttpServletRequest req, HttpServletResponse res) throws Exception;
}

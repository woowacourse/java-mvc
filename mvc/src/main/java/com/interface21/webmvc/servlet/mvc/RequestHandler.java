package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface RequestHandler {

    ModelAndView handle(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception;
}

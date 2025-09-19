package com.interface21.webmvc.servlet.controller;

import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {

    ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}

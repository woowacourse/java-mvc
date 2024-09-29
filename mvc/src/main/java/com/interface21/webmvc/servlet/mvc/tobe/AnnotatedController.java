package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;

public interface AnnotatedController {

    ModelAndView save(final HttpServletRequest req, final HttpServletResponse res);

    ModelAndView show(final HttpServletRequest req, final HttpServletResponse res);
}

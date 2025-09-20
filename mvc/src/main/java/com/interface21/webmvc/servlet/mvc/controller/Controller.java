package com.interface21.webmvc.servlet.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}

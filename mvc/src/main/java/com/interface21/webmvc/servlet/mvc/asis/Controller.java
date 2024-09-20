package com.interface21.webmvc.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    Object execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}

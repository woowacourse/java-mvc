package com.interface21.webmvc.servlet.mvc.legacy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Deprecated(since = "step3", forRemoval = true)
public interface Controller {
    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}

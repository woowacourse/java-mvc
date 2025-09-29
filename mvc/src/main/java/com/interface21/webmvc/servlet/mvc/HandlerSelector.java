package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerSelector {

    Object getHandler(HttpServletRequest request);
}

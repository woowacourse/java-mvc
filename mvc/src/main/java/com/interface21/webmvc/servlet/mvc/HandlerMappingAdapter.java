package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMappingAdapter {

    void initialize();

    HandlerAdapter getHandler(HttpServletRequest request);
}

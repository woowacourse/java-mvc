package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Handler {

    void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}

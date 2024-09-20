package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ViewResolver {

    void resolveView(Object mv, HttpServletRequest request, HttpServletResponse response) throws Exception;
}

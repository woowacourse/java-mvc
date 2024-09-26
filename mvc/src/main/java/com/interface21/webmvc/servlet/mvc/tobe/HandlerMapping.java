package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

	void initialize() throws Exception;

	Handler getHandler(HttpServletRequest request);

	boolean canService(HttpServletRequest request);
}

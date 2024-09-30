package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

	@Override
	public boolean canHandle(Object handler) {
		return (handler instanceof HandlerExecution);
	}

	@Override
	public ModelAndView adapt(Object handler, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		return ((HandlerExecution) handler).handle(request, response);
	}
}

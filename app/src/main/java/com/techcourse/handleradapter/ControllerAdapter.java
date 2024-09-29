package com.techcourse.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdapter implements HandlerAdapter {

	@Override
	public boolean canHandle(Object handler) {
		return (handler instanceof Controller);
	}

	@Override
	public ModelAndView adapt(Object handler, HttpServletRequest request,
		HttpServletResponse response) {
		return ((Controller) handler).execute(request, response);
	}
}
